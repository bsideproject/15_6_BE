package bside.NotToDoClub.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DailyBadgeJobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * 하루에 한번 획득되는 뱃지에 대한 batch
     * - [step1] 패배는 성공의 어머니 - 낫투두 1일 절제 기록 결과 실패 (낫투두 처음 실패)
     * - [step2] 인내의 달인 - 하루 동안 절제 기록이 모두 ‘성공’ (일일 절제기록이 3개 이상일 때만)
     */
    @Bean
    public Job stepDailyBadgeJob(){
        return jobBuilderFactory.get("stepDailyBadgeJob")
                .start(step1ForDaily())
                .next(step2ForDaily())
                .build();
    }

    @Bean
    public Step step1ForDaily(){
        return stepBuilderFactory.get("step1ForDaily")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("Granting \"the Failure is the mother of success\"" +
                            "(패배는 성공의 어머니) badge step");

                    return RepeatStatus.FINISHED;
                })).build();
    }

    @Bean
    public Step step2ForDaily(){
        return stepBuilderFactory.get("step2ForDaily")
                .tasklet(((contribution, chunkContext) -> {
                    log.info("Grant \"Master of Patience\"(인내의 달인) badge step");

                    return RepeatStatus.FINISHED;
                })).build();
    }
}
