package com.art.gateway.listener;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RouteConfDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1L;

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 路由id
     */
    private String routeId;

    /**
     * 断言
     */
    private String predicates;

    /**
     * 过滤器
     */
    private String filters;

    /**
     * uri
     */
    private String uri;

    /**
     * 排序
     */
    private Integer sortOrder;

    /**
     * 路由元信息
     */
    private String metadata;

}