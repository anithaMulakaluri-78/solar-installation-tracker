package com.solartracker.dto.response;
import lombok.*;
@Getter @Builder @AllArgsConstructor public class ReviewResponse { private Long installationId; private boolean consumerVerified, inverterCaptured, locationCaptured, readyForSubmission; private int panelCount, requiredPhotoCount, uploadedPhotoCount; }
