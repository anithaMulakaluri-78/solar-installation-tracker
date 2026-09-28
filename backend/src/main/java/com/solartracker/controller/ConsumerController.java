package com.solartracker.controller;
import com.solartracker.dto.request.ConsumerRequest; import com.solartracker.dto.response.ConsumerResponse; import com.solartracker.service.ConsumerService; import io.swagger.v3.oas.annotations.tags.Tag; import jakarta.validation.Valid; import lombok.RequiredArgsConstructor; import org.springframework.http.ResponseEntity; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/consumers") @RequiredArgsConstructor @Tag(name="Consumers")
public class ConsumerController {
 private final ConsumerService service;
 @GetMapping @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','INSTALLER')") public java.util.List<ConsumerResponse> list(@RequestParam(required=false) String search){return service.list(search);}
 @GetMapping("/{id}") @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','INSTALLER')") public ConsumerResponse get(@PathVariable Long id){return service.get(id);}
 @GetMapping("/by-number/{number}") @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN','INSTALLER')") public ConsumerResponse byNumber(@PathVariable String number){return service.byNumber(number);}
 @PostMapping @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')") public ResponseEntity<ConsumerResponse> create(@Valid @RequestBody ConsumerRequest r){return ResponseEntity.status(201).body(service.create(r));}
 @PutMapping("/{id}") @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')") public ConsumerResponse update(@PathVariable Long id,@Valid @RequestBody ConsumerRequest r){return service.update(id,r);}
 @DeleteMapping("/{id}") @PreAuthorize("hasRole('SUPER_ADMIN')") public ResponseEntity<Void> delete(@PathVariable Long id){service.delete(id);return ResponseEntity.noContent().build();}
}
