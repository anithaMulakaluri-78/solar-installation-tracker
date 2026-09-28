package com.solartracker.repository;
import com.solartracker.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;
public interface ConsumerRepository extends JpaRepository<Consumer, Long>, JpaSpecificationExecutor<Consumer> {
    Optional<Consumer> findByConsumerNumber(String consumerNumber);
    boolean existsByConsumerNumber(String consumerNumber);
    boolean existsByConsumerNumberAndIdNot(String consumerNumber, Long id);
}
