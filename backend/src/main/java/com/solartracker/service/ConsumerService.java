package com.solartracker.service;
import com.solartracker.dto.request.ConsumerRequest; import com.solartracker.dto.response.ConsumerResponse; import java.util.List;
public interface ConsumerService { List<ConsumerResponse> list(String search); ConsumerResponse get(Long id); ConsumerResponse byNumber(String number); ConsumerResponse create(ConsumerRequest r); ConsumerResponse update(Long id, ConsumerRequest r); void delete(Long id); }
