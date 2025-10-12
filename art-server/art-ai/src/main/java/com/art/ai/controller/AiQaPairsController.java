package com.art.ai.controller;

import com.art.ai.core.dto.qa.AiQaPairsDTO;
import com.art.ai.core.dto.qa.AiQaPairsPageDTO;
import com.art.ai.core.dto.qa.AiQaSimilarQuestionDTO;
import com.art.ai.service.qa.AiQaPairsService;
import com.art.core.common.model.PageResult;
import com.art.core.common.model.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * QA问答对控制器
 *
 * @author fxz
 * @since 2025/10/12
 */
@Tag(name = "QA问答对管理")
@RestController
@RequestMapping("/ai/qa/pairs")
@RequiredArgsConstructor
public class AiQaPairsController {

	private final AiQaPairsService qaPairsService;

	/**
	 * 添加QA对
	 */
	@Operation(summary = "添加QA对")
	@PostMapping("/add")
	public Result<AiQaPairsDTO> add(@RequestBody AiQaPairsDTO dto) {
		return Result.success(qaPairsService.addQaPair(dto));
	}

	/**
	 * 修改QA对
	 */
	@Operation(summary = "修改QA对")
	@PostMapping("/update")
	public Result<Boolean> update(@RequestBody AiQaPairsDTO dto) {
		return Result.success(qaPairsService.updateQaPair(dto));
	}

	/**
	 * 删除QA对
	 */
	@Operation(summary = "删除QA对")
	@DeleteMapping("/delete")
	public Result<Boolean> delete(Long id) {
		return Result.judge(qaPairsService.deleteQaPair(id));
	}

	/**
	 * 获取单条
	 */
	@Operation(summary = "获取单条")
	@GetMapping("/findById")
	public Result<AiQaPairsDTO> findById(@RequestParam Long id) {
		return Result.success(qaPairsService.findById(id));
	}

	/**
	 * 分页查询
	 */
	@Operation(summary = "分页查询")
	@GetMapping("/page")
	public Result<PageResult<AiQaPairsDTO>> page(AiQaPairsPageDTO pageDTO) {
		return Result.success(PageResult.success(qaPairsService.page(pageDTO)));
	}

	/**
	 * 根据数据集ID查询所有QA对
	 */
	@Operation(summary = "根据数据集ID查询所有QA对")
	@GetMapping("/listByDatasetId")
	public Result<List<AiQaPairsDTO>> listByDatasetId(@RequestParam Long datasetId) {
		return Result.success(qaPairsService.listByDatasetId(datasetId));
	}

	/**
	 * 启用/禁用QA对
	 */
	@Operation(summary = "启用/禁用QA对")
	@PostMapping("/toggleEnabled")
	public Result<Boolean> toggleEnabled(@RequestBody AiQaPairsDTO qaPairsDTO) {
		return Result.success(qaPairsService.toggleEnabled(qaPairsDTO.getId(), qaPairsDTO.getEnabled()));
	}

	/**
	 * 添加相似问题
	 */
	@Operation(summary = "添加相似问题")
	@PostMapping("/similarQuestion/add")
	public Result<AiQaSimilarQuestionDTO> addSimilarQuestion(@RequestBody AiQaSimilarQuestionDTO dto) {
		return Result.success(qaPairsService.addSimilarQuestion(dto));
	}

	/**
	 * 删除相似问题
	 */
	@Operation(summary = "删除相似问题")
	@DeleteMapping("/similarQuestion/delete")
	public Result<Boolean> deleteSimilarQuestion(Long id) {
		return Result.judge(qaPairsService.deleteSimilarQuestion(id));
	}

	/**
	 * 查询QA对的所有相似问题
	 */
	@Operation(summary = "查询QA对的所有相似问题")
	@GetMapping("/similarQuestion/list")
	public Result<List<AiQaSimilarQuestionDTO>> listSimilarQuestions(@RequestParam Long qaPairId) {
		return Result.success(qaPairsService.listSimilarQuestions(qaPairId));
	}

}
