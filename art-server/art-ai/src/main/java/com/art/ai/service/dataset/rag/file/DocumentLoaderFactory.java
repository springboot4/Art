package com.art.ai.service.dataset.rag.file;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.parser.apache.poi.ApachePoiDocumentParser;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * 文档加载器工厂类
 *
 * @author fxz
 */
@UtilityClass
public final class DocumentLoaderFactory {

	private static final Map<String, DocumentParser> PARSER_REGISTRY;

	static {
		final Map<String, DocumentParser> parsers = new HashMap<>();

		parsers.put("txt", new TextDocumentParser());
		parsers.put("pdf", new ApachePdfBoxDocumentParser());
		Set.of("doc", "docx", "ppt", "pptx", "xls", "xlsx")
			.forEach(ext -> parsers.put(ext, new ApachePoiDocumentParser()));

		PARSER_REGISTRY = Collections.unmodifiableMap(parsers);
	}

	/**
	 * 根据 URL 和文件扩展名加载文档。
	 *
	 * <p>
	 * 该方法会根据文件扩展名自动选择合适的解析器来加载文档内容。 如果找不到支持的扩展名，将返回一个空的 Optional。
	 * @param url 文档的 URL 路径
	 * @param ext 文件的扩展名 (大小写不敏感)
	 * @return 包含 Document 的 Optional，如果扩展名不支持则为空
	 */
	public static Optional<Document> loadDocument(String url, String ext) {
		return Optional.ofNullable(ext)
			.map(String::toLowerCase)
			.map(PARSER_REGISTRY::get)
			.map(parser -> UrlDocumentLoader.load(url, parser));
	}

}