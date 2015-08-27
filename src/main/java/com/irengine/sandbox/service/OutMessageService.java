package com.irengine.sandbox.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.sandbox.domain.OutMessage;
import com.irengine.sandbox.repository.OutMessageRepository;

@Service
@Transactional
public class OutMessageService {

	@Inject
	private OutMessageRepository outMessageRepository;
	
	public List<OutMessage> findAll() {
		return (List<OutMessage>) outMessageRepository.findAll(new Sort(Sort.Direction.DESC, "id"));
	}

	public OutMessage findOneById(long id) {
		return outMessageRepository.findOne(id);
	}

	public OutMessage save(OutMessage message) {
		return outMessageRepository.save(message);
	}

	public List<OutMessage> findAllByType(String type) {
		return outMessageRepository.findAllByType(type);
	}
	
	public long count() {
		return outMessageRepository.count();
	}

	public void deleteOneById(Long id) {
		outMessageRepository.delete(id);
	}

}
