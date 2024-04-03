/*
 *   COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.art.demos.mapstruct.convert;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.art.demos.mapstruct.bo.ArtBO;
import com.art.demos.mapstruct.domain.ArtDO;
import com.art.demos.mapstruct.dto.ArtDTO;
import com.art.demos.mapstruct.vo.ArtVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/22 21:22
 */
@Mapper
public interface ArtConvert {

	/**
	 * 获取ArtConvert的实现类
	 */
	ArtConvert INSTANCE = Mappers.getMapper(ArtConvert.class);

	/**
	 * DO---->DTO
	 */
	@Mappings({ @Mapping(source = "id", target = "userId"),
			@Mapping(source = "value", target = "value", qualifiedByName = "convertStringToList") })
	ArtDTO convert(ArtDO artDO);

	/**
	 * DTO--->VO
	 */
	@Mappings({ @Mapping(target = "userId", ignore = true),
			@Mapping(source = "userId", target = "userName", qualifiedByName = "convertIdToName"),
			@Mapping(source = "value", target = "value", qualifiedByName = "convertListToString") })
	ArtVO convert(ArtDTO artDTO);

	/**
	 * DO + VO ---> BO
	 */
	@Mappings({ @Mapping(source = "artDO.value", target = "a"), @Mapping(source = "artVO.userName", target = "b") })
	ArtBO convert(ArtDO artDO, ArtVO artVO);

	void updateVO(ArtDO artDO, @MappingTarget ArtVO artVO);

	@Named("convertStringToList")
	default List<String> convertStringToList(String val) {
		if (StrUtil.isBlank(val)) {
			return new ArrayList<>(0);
		}

		return Arrays.asList(val.split(","));
	}

	@Named("convertListToString")
	default String convertListToString(List<String> val) {
		if (CollectionUtil.isEmpty(val)) {
			return null;
		}

		return String.join(",", val);
	}

	@Named("convertIdToName")
	default String convertIdToName(Long id) {
		return "用户:" + id;
	}

}
