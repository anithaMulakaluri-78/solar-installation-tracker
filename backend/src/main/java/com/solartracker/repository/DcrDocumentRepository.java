package com.solartracker.repository;
import com.solartracker.entity.DcrDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DcrDocumentRepository extends JpaRepository<DcrDocument, Long> {
    List<DcrDocument> findByDcrIdOrderByUploadedAtDesc(Long dcrId);
}
