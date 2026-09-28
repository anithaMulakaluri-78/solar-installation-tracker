package com.solartracker.repository.spec;

import com.solartracker.entity.Installation;
import com.solartracker.entity.enums.DcrStatus;
import com.solartracker.entity.enums.InstallationStatus;
import org.springframework.data.jpa.domain.Specification;

public final class InstallationSpecifications {

    private InstallationSpecifications() {}

    public static Specification<Installation> hasDcrStatus(DcrStatus dcrStatus) {
        return (root, query, cb) -> dcrStatus == null ? null : cb.equal(root.get("dcrStatus"), dcrStatus);
    }

    public static Specification<Installation> hasStatus(InstallationStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Installation> hasSection(String section) {
        return (root, query, cb) -> (section == null || section.isBlank())
                ? null
                : cb.equal(cb.lower(root.get("section")), section.toLowerCase());
    }

    // Matches ULA application ID, service number, or consumer name
    public static Specification<Installation> search(String term) {
        return (root, query, cb) -> {
            if (term == null || term.isBlank()) {
                return null;
            }
            String like = "%" + term.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("ulaApplicationId")), like),
                    cb.like(root.get("serviceNumber"), like),
                    cb.like(cb.lower(root.get("consumerName")), like),
                    cb.like(cb.lower(root.join("consumer").get("consumerNumber")), like)
            );
        };
    }
}
