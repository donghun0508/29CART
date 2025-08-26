package com.loopers.data;

import com.loopers.domain.catalog.Brand;
import com.loopers.fixture.BrandFixture;
import com.loopers.fixture.UserFixture;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@Disabled("데이터 초기화용 - 일반 테스트에서 제외")
@Slf4j
@SpringBootTest
@ActiveProfiles("local")
@TestPropertySource(properties = {
    "spring.test.database.replace=none",
    "logging.level.org.hibernate.SQL=DEBUG",
    "logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE"
})
class DataInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 500,000건 -> 50초
    @Test
    void userDataInitializer() throws InterruptedException {
        final int TOTAL_BATCHES = 50;
        final int BULK_INSERT_SIZE = 10000;

        AtomicInteger counter = new AtomicInteger(1);
        AtomicInteger ac = new AtomicInteger(1);

        String sql = "INSERT INTO users (account_id, email, birth, gender, created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())";

        for (int batch = 0; batch < TOTAL_BATCHES; batch++) {
            List<Object[]> batchArgs = UserFixture.createBulk(BULK_INSERT_SIZE)
                .stream()
                .map(user -> new Object[]{
                    ac.getAndIncrement() + "AC",
                    counter.getAndIncrement() + "@gmail.com",
                    user.getBirth().day(),
                    user.getGender().name()
                })
                .collect(Collectors.toCollection(() -> new ArrayList<>(BULK_INSERT_SIZE)));

            jdbcTemplate.batchUpdate(sql, batchArgs);
            log.info("Batch {} completed", batch);
        }

        log.info("All batches completed. Total inserted: 500,000");
    }

    // 브랜드 10,000건 -> 1초
    @Test
    void brandDataInitializer() {
        int batchSize = 10000;

        List<Brand> brands = IntStream.range(0, batchSize)
            .mapToObj(i -> BrandFixture.persistence().build())
            .toList();

        String sql = "INSERT INTO brand (name, created_at, updated_at) VALUES (?, ?, ?)";

        ZonedDateTime now = ZonedDateTime.now();

        jdbcTemplate.batchUpdate(sql, brands, batchSize, (ps, brand) -> {
            ps.setString(1, brand.getName());
            ps.setTimestamp(2, Timestamp.from(now.toInstant()));
            ps.setTimestamp(3, Timestamp.from(now.toInstant()));
        });
    }

    // 상품 10,000,000건 -> 1분 30초
    @Test
    void productDataInitializer() {
        final int EXECUTE_COUNT = 500;
        final int BULK_INSERT_SIZE = 2000;
        final int threadCount = 10;

        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            for (int i = 0; i < threadCount; i++) {
                executorService.submit(() -> {
                    String sql = "INSERT INTO product (brand_id, heart_count, name, unit_price, stock, version, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

                    ThreadLocalRandom random = ThreadLocalRandom.current();

                    for (int batch = 0; batch < EXECUTE_COUNT; batch++) {
                        List<Object[]> batchArgs = new ArrayList<>();

                        for (int j = 0; j < BULK_INSERT_SIZE; j++) {
                            long randomBrandId = random.nextLong(1, 10001);
                            LocalDateTime randomDateTime = generateRandomDateTime(random);
                            Timestamp timestamp = Timestamp.valueOf(randomDateTime);

                            batchArgs.add(new Object[]{
                                randomBrandId,
                                0L,
                                "Product-" + randomBrandId + "-" + j,
                                random.nextLong(1000, 10000001), // current() 호출 제거
                                random.nextLong(1, 100000),       // current() 호출 제거
                                0L,
                                timestamp,
                                timestamp
                            });
                        }

                        jdbcTemplate.batchUpdate(sql, batchArgs);
                        log.info("Thread {} - Batch {} completed", Thread.currentThread().getName(), batch);
                    }
                });
            }
            executorService.shutdown();
        }
    }

    @Test
    void heartDataInitializer() throws InterruptedException {
        final int TARGET_COUNT = 50_000_000;
        final int BULK_INSERT_SIZE = 20_000;
        final int threadCount = 8;

        log.info("=== 1단계: 중복 없는 데이터 생성 ===");

        // 1단계: 중복 없는 (user_id, target_id) 조합 생성
        Set<String> uniquePairs = ConcurrentHashMap.newKeySet();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        while (uniquePairs.size() < TARGET_COUNT) {
            long userId = random.nextLong(1, 500_001);
            long targetId = random.nextLong(1, 10_000_001);
            String pair = userId + "-" + targetId;

            uniquePairs.add(pair); // Set이므로 자동으로 중복 제거

            if (uniquePairs.size() % 1_000_000 == 0) {
                log.info("생성된 고유 조합: {}/{}", uniquePairs.size(), TARGET_COUNT);
            }
        }

        log.info("=== 2단계: 데이터 분할 및 삽입 ===");

        // 2단계: List로 변환하고 스레드별로 분할
        List<String> pairsList = new ArrayList<>(uniquePairs);
        uniquePairs = null; // 메모리 해제

        int pairsPerThread = TARGET_COUNT / threadCount;
        CountDownLatch latch = new CountDownLatch(threadCount);

        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            for (int threadId = 0; threadId < threadCount; threadId++) {
                int startIndex = threadId * pairsPerThread;
                int endIndex = (threadId == threadCount - 1) ? TARGET_COUNT : (threadId + 1) * pairsPerThread;

                executorService.submit(() -> {
                    try {
                        String sql = "INSERT INTO heart (user_id, target_id, target_type, created_at, updated_at) VALUES (?, ?, ?, NOW(), NOW())";

                        for (int i = startIndex; i < endIndex; i += BULK_INSERT_SIZE) {
                            List<Object[]> batchArgs = new ArrayList<>();

                            for (int j = i; j < Math.min(i + BULK_INSERT_SIZE, endIndex); j++) {
                                String[] parts = pairsList.get(j).split("-");
                                long userId = Long.parseLong(parts[0]);
                                long targetId = Long.parseLong(parts[1]);

                                batchArgs.add(new Object[]{
                                    userId,
                                    targetId,
                                    "PRODUCT"
                                });
                            }

                            jdbcTemplate.batchUpdate(sql, batchArgs);

                            if ((i / BULK_INSERT_SIZE) % 50 == 0) { // 50배치마다 로그
                                log.info("Thread-{}: {}% completed",
                                    Thread.currentThread().getId(),
                                    (int)((i - startIndex) * 100.0 / (endIndex - startIndex)));
                            }
                        }

                        log.info("Thread-{}: 완료! ({} ~ {})",
                            Thread.currentThread().getId(), startIndex, endIndex);

                    } catch (Exception e) {
                        log.error("Thread-{} error: ", Thread.currentThread().getId(), e);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            executorService.shutdown();
            latch.await();

            log.info("=== 완료 ===");
            log.info("정확히 {}건 삽입 완료!", TARGET_COUNT);
        }
    }

    private LocalDateTime generateRandomDateTime(ThreadLocalRandom random) {
        LocalDateTime start = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 8, 13, 23, 59, 59);

        long startEpoch = start.toEpochSecond(ZoneOffset.UTC);
        long endEpoch = end.toEpochSecond(ZoneOffset.UTC);

        long randomEpoch = random.nextLong(startEpoch, endEpoch);
        return LocalDateTime.ofEpochSecond(randomEpoch, 0, ZoneOffset.UTC);
    }
}
