/*
 * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.art.common.security.core.config;

import com.art.common.security.core.service.user.ArtUserDetailServiceImpl;
import com.art.common.security.core.service.user.ArtUserManager;
import com.art.system.api.menu.MenuServiceApi;
import com.art.system.api.user.UserServiceApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author fxz
 * @version 0.0.1
 * @date 2023/4/13 15:29
 */
@AutoConfiguration
public class ArtUserDetailServiceAutoConfigure {

	@SuppressWarnings("all")
	@Bean
	public ArtUserManager artUserManager(UserServiceApi userService, MenuServiceApi menuService) {
		return new ArtUserManager(userService, menuService);
	}

	@Bean
	public ArtUserDetailServiceImpl artUserDetailService(ArtUserManager artUserManager) {
		return new ArtUserDetailServiceImpl(artUserManager);
	}

}
