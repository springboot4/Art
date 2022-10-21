package com.fxz.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fxz.common.mp.result.PageResult;
import com.fxz.common.mp.result.Result;
import com.fxz.system.dto.PostDto;
import com.fxz.system.entity.Post;
import com.fxz.system.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息表
 *
 * @author fxz
 * @date 2022-04-05
 */
@Tag(name = "岗位管理")
@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	/**
	 * 添加
	 */
	@Operation(summary = "添加")
	@PostMapping(value = "/add")
	public Result<Boolean> add(@RequestBody PostDto postDto) {
		return Result.success(postService.addPost(postDto));
	}

	/**
	 * 修改
	 */
	@Operation(summary = "修改")
	@PostMapping(value = "/update")
	public Result<Boolean> update(@RequestBody PostDto postDto) {
		return Result.success(postService.updatePost(postDto));
	}

	/**
	 * 删除
	 */
	@Operation(summary = "删除")
	@DeleteMapping(value = "/delete")
	public Result<Boolean> delete(Long id) {
		return Result.judge(postService.deletePost(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping(value = "/findById")
	public Result<Post> findById(Long id) {
		return Result.success(postService.findById(id));
	}

	/**
	 * 获取全部
	 */
	@Operation(summary = "获取全部")
	@GetMapping(value = "/findAll")
	public Result<List<Post>> findAll() {
		return Result.success(postService.findAll());
	}

	/**
	 * 分页
	 */
	@Operation(summary = "分页")
	@GetMapping(value = "/page")
	public Result<PageResult<Post>> pagePost(Page<Post> pageParam, Post post) {
		return Result.success(PageResult.success(postService.pagePost(pageParam, post)));
	}

}