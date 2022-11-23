/*
 * COPYRIGHT (C) 2022 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
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

package com.art.demos.mapstruct;

import com.art.demos.mapstruct.bo.ArtBO;
import com.art.demos.mapstruct.convert.ArtConvert;
import com.art.demos.mapstruct.domain.ArtDO;
import com.art.demos.mapstruct.dto.ArtDTO;
import com.art.demos.mapstruct.vo.ArtVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/22 21:13
 */
@Slf4j
@SpringBootTest
public class MapstructTest {

	@Test
	public void mapstructTest() {
		ArtDO artDO = ArtDO.builder().id(1L).value("a,b,c,d,e").build();
		ArtDTO artDTO = ArtConvert.INSTANCE.convert(artDO);
		log.info("artDO:{}--->artDTO:{}", artDO, artDTO);

		ArtVO artVO = ArtConvert.INSTANCE.convert(artDTO);
		log.info("artDTO:{}--->artVO:{}", artDTO, artVO);

		ArtBO artBO = ArtConvert.INSTANCE.convert(artDO, artVO);
		log.info("artBO:{}", artBO);

		artDO.setValue("update");
		ArtConvert.INSTANCE.updateVO(artDO, artVO);
		log.info("update:{}", artVO);
	}

}
