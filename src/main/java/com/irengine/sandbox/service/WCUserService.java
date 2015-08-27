package com.irengine.sandbox.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.irengine.sandbox.domain.WCUser;
import com.irengine.sandbox.repository.WCUserRepository;
import com.irengine.sandbox.web.rest.util.PaginationUtil;

@Service
@Transactional
public class WCUserService {

	@Autowired
	private WCUserRepository wcUserRepository;

	public WCUser save(WCUser wcUser) {
		return wcUserRepository.save(wcUser);
	}

	public WCUser findOneByOpenId(String openId) {
		return wcUserRepository.findOneByOpenId(openId);
	}

	public Page<WCUser> findAll(Integer offset, Integer limit) {
		return wcUserRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
	}

	public WCUser findOne(Long id) {
		return wcUserRepository.findOne(id);
	}

	public void delete(Long id) {
		wcUserRepository.delete(id);
	}

}
