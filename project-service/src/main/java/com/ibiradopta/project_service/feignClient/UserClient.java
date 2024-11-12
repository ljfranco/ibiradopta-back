package com.ibiradopta.project_service.feignClient;

import com.ibiradopta.project_service.configuration.feign.OAuthFeignConfig;
import com.ibiradopta.project_service.models.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "user-service", url = "${USER_SERVICE_URL}", configuration = OAuthFeignConfig.class)
public interface UserClient {
    @RequestMapping(method = RequestMethod.GET, value = "/users/id/{id}")
    UserDto getUserById(@PathVariable("id") String id);
}
