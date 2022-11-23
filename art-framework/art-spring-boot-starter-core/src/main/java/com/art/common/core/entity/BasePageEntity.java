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

package com.art.common.core.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Fxz
 * @version 0.0.1
 * @date 2022/11/23 00:10
 */
@Data
@Schema(title = "分页参数")
public class BasePageEntity implements Serializable {

    private static final long serialVersionUID = 7489222986629492487L;

    /**
     * 当前页
     */
    @Schema(description = "当前页",defaultValue = "1")
    private int current = 1;

    /**
     * 每页显示条数，默认 10
     */
    @Schema(description = "每页记录数",defaultValue = "10")
    private int size = 10;

}
