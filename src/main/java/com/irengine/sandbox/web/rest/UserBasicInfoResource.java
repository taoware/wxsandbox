package com.irengine.sandbox.web.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.irengine.sandbox.domain.OutNewsMessageItem;
import com.irengine.sandbox.domain.UserBasicInfo;
import com.irengine.sandbox.domain.UserBasicInfo.USERSTATUS;
import com.irengine.sandbox.repository.OutNewsMessageItemRepository;
import com.irengine.sandbox.repository.UserBasicInfoRepository;
import com.irengine.sandbox.web.rest.util.PaginationUtil;
import com.irengine.sandbox.web.rest.util.RestTemplateUtil;

/**
 * REST controller for managing UserBasicInfo.
 */
@RestController
@RequestMapping("/api")
public class UserBasicInfoResource {

    private final Logger log = LoggerFactory.getLogger(UserBasicInfoResource.class);

    @Inject
    private UserBasicInfoRepository userBasicInfoRepository;
    
    @Inject
    private OutNewsMessageItemRepository outNewsMessageItemRepository;

    @RequestMapping(value = "/userBasicInfos/c")
    @Timed
    public void createByGet(@RequestParam("id") Long id,@RequestParam("mobile") String mobile,
    		@RequestParam(value="itemId") Long itemId,
    		HttpServletResponse response,HttpServletRequest request) throws IOException, ServletException{
    	log.debug("调用/userBasicInfos/c接口:id:"+id+",mobile:"+mobile+",itemId:"+itemId);
    	UserBasicInfo userBasicInfo=userBasicInfoRepository.findOne(id);
    	userBasicInfo.setMobile(mobile);
    	userBasicInfo=userBasicInfoRepository.save(userBasicInfo);
    	/*注册金诚通并修改status*/
    	try {
			Map<String,String> returnMap=RestTemplateUtil.validate(userBasicInfo.getOpenId(), userBasicInfo.getMobile());
			log.debug("注册/登录金诚通:returnMap:"+returnMap);
			if(returnMap.get("url")!=null){
				//注册/登录成功
				log.debug("userBasicInfo:"+userBasicInfo+".注册/登录金诚通成功");
				//更改用户status
				userBasicInfo.setStatus(USERSTATUS.registered);
				userBasicInfoRepository.save(userBasicInfo);
		    	/*根据itemId决定跳转到哪个页面*/
		    	if(itemId==-1){
		    		//跳转到个人中心
		    		log.debug("跳转到个人中心");
		    		log.debug("金诚通url:"+returnMap.get("url"));
		    		response.sendRedirect(returnMap.get("url"));
		    		//request.getRequestDispatcher("http://180.166.29.246:8089/mediawap/").forward(request, response);
		    	}else{
		    		OutNewsMessageItem outNewsMessageItem=outNewsMessageItemRepository.findOne(itemId);
		    		log.debug("跳转到子图文id为"+itemId+"对应的url.url:"+outNewsMessageItem.toString());
		    		response.sendRedirect(outNewsMessageItem.getUrl());
		    		//request.getRequestDispatcher(outNewsMessageItem.getUrl()).forward(request, response);
		    	}
			}else{
				//注册/登录失败
				log.debug("userBasicInfo:"+userBasicInfo+".注册/登录金诚通失败");
				//处理(如何处理?)
				
			}
		} catch (Exception e) {
			log.debug("注册/登录金诚通失败");
			e.printStackTrace();
		}

    }
    
    /**
     * POST  /userBasicInfos -> Create a new userBasicInfo.
     */
    @RequestMapping(value = "/userBasicInfos",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody UserBasicInfo userBasicInfo) throws URISyntaxException {
        log.debug("REST request to save UserBasicInfo : {}", userBasicInfo);
        if (userBasicInfo.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new userBasicInfo cannot already have an ID").build();
        }
        userBasicInfo.setStatus(USERSTATUS.unregistered);
        userBasicInfoRepository.save(userBasicInfo);
        return ResponseEntity.created(new URI("/api/userBasicInfos/" + userBasicInfo.getId())).build();
    }

    /**
     * PUT  /userBasicInfos -> Updates an existing userBasicInfo.
     */
    @RequestMapping(value = "/userBasicInfos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody UserBasicInfo userBasicInfo) throws URISyntaxException {
        log.debug("REST request to update UserBasicInfo : {}", userBasicInfo);
        if (userBasicInfo.getId() == null) {
            return create(userBasicInfo);
        }
        userBasicInfoRepository.save(userBasicInfo);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /userBasicInfos -> get all the userBasicInfos.
     */
    @RequestMapping(value = "/userBasicInfos",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserBasicInfo>> getAll(@RequestParam(value = "page" , required = false) Integer offset,
                                  @RequestParam(value = "per_page", required = false) Integer limit)
        throws URISyntaxException {
        Page<UserBasicInfo> page = userBasicInfoRepository.findAll(PaginationUtil.generatePageRequest(offset, limit));
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/userBasicInfos", offset, limit);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /userBasicInfos/:id -> get the "id" userBasicInfo.
     */
    @RequestMapping(value = "/userBasicInfos/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserBasicInfo> get(@PathVariable Long id) {
        log.debug("REST request to get UserBasicInfo : {}", id);
        return Optional.ofNullable(userBasicInfoRepository.findOne(id))
            .map(userBasicInfo -> new ResponseEntity<>(
                userBasicInfo,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userBasicInfos/:id -> delete the "id" userBasicInfo.
     */
    @RequestMapping(value = "/userBasicInfos/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete UserBasicInfo : {}", id);
        userBasicInfoRepository.delete(id);
    }
}
