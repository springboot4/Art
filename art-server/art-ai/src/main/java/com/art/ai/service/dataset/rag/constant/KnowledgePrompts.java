package com.art.ai.service.dataset.rag.constant;

import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.GRAPH_COMPLETION_DELIMITER;
import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.GRAPH_ENTITY_EXTRACTION_ENTITY_TYPES;
import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.GRAPH_RECORD_DELIMITER;
import static com.art.ai.service.dataset.rag.constant.KnowledgeConstants.GRAPH_TUPLE_DELIMITER;

/**
 * @author fxz
 * @since 2025/9/15 13:02
 */
public interface KnowledgePrompts {

	String GRAPH_EXTRACTION_PROMPT_CN = """
			-目标-
			给定一个可能与此活动相关的文本文档以及实体类型列表，从文本中识别出所有这些类型的实体以及识别出的实体之间的所有关系。

			-步骤-
			1. 识别所有实体。对于每个识别出的实体，提取以下信息：
			- entity_name：实体的名称，首字母大写
			- entity_type：以下类型之一：[{entity_types}]
			- entity_description：实体的属性和活动的全面描述
			将每个实体格式化为 ("entity"{tuple_delimiter}<entity_name>{tuple_delimiter}<entity_type>{tuple_delimiter}<entity_description>)

			2. 从步骤1中识别出的实体中，识别出所有明确相关的 (source_entity, target_entity) 对。
			对于每对相关的实体，提取以下信息：
			- source_entity：在步骤1中识别的源实体的名称
			- target_entity：在步骤1中识别的目标实体的名称
			- relationship_description：解释你认为源实体和目标实体之间相关的原因
			- relationship_strength：一个表示源实体和目标实体之间关系强度的数字分数
			将每个关系格式化为 ("relationship"{tuple_delimiter}<source_entity>{tuple_delimiter}<target_entity>{tuple_delimiter}<relationship_description>{tuple_delimiter}<relationship_strength>)

			3. 以英文返回输出，作为所有在步骤1和步骤2中识别的实体和关系的列表。使用 **{record_delimiter}** 作为列表分隔符。

			4. 完成时，输出 {completion_delimiter}

			######################
			-示例-
			######################
			示例 1:
			Entity_types: ORGANIZATION,PERSON
			文本:
			The Verdantis's Central Institution is scheduled to meet on Monday and Thursday, with the institution planning to release its latest policy decision on Thursday at 1:30 p.m. PDT, followed by a press conference where Central Institution Chair Martin Smith will take questions. Investors expect the Market Strategy Committee to hold its benchmark interest rate steady in a range of 3.5%-3.75%.
			######################
			输出:
			("entity"{tuple_delimiter}CENTRAL INSTITUTION{tuple_delimiter}ORGANIZATION{tuple_delimiter}The Central Institution is the Federal Reserve of Verdantis, which is setting interest rates on Monday and Thursday)
			{record_delimiter}
			("entity"{tuple_delimiter}MARTIN SMITH{tuple_delimiter}PERSON{tuple_delimiter}Martin Smith is the chair of the Central Institution)
			{record_delimiter}
			("entity"{tuple_delimiter}MARKET STRATEGY COMMITTEE{tuple_delimiter}ORGANIZATION{tuple_delimiter}The Central Institution committee makes key decisions about interest rates and the growth of Verdantis's money supply)
			{record_delimiter}
			("relationship"{tuple_delimiter}MARTIN SMITH{tuple_delimiter}CENTRAL INSTITUTION{tuple_delimiter}Martin Smith is the Chair of the Central Institution and will answer questions at a press conference{tuple_delimiter}9)
			{completion_delimiter}

			######################
			示例 2:
			Entity_types: ORGANIZATION
			文本:
			TechGlobal's (TG) stock skyrocketed in its opening day on the Global Exchange Thursday. But IPO experts warn that the semiconductor corporation's debut on the public markets isn't indicative of how other newly listed companies may perform.

			TechGlobal, a formerly public company, was taken private by Vision Holdings in 2014. The well-established chip designer says it powers 85% of premium smartphones.
			######################
			输出:
			("entity"{tuple_delimiter}TECHGLOBAL{tuple_delimiter}ORGANIZATION{tuple_delimiter}TechGlobal is a stock now listed on the Global Exchange which powers 85% of premium smartphones)
			{record_delimiter}
			("entity"{tuple_delimiter}VISION HOLDINGS{tuple_delimiter}ORGANIZATION{tuple_delimiter}Vision Holdings is a firm that previously owned TechGlobal)
			{record_delimiter}
			("relationship"{tuple_delimiter}TECHGLOBAL{tuple_delimiter}VISION HOLDINGS{tuple_delimiter}Vision Holdings formerly owned TechGlobal from 2014 until present{tuple_delimiter}5)
			{completion_delimiter}

			######################
			示例 3:
			Entity_types: ORGANIZATION,LOCATION,PERSON
			文本:
			Five Aurelians jailed for 8 years in Firuzabad and widely regarded as hostages are on their way home to Aurelia.

			The swap orchestrated by Quintara was finalized when $8bn of Firuzi funds were transferred to financial institutions in Krohaara, the capital of Quintara.

			The exchange initiated in Firuzabad's capital, Tiruzia, led to the four men and one woman, who are also Firuzi nationals, boarding a chartered flight to Krohaara.

			They were welcomed by senior Aurelian officials and are now on their way to Aurelia's capital, Cashion.

			The Aurelians include 39-year-old businessman Samuel Namara, who has been held in Tiruzia's Alhamia Prison, as well as journalist Durke Bataglani, 59, and environmentalist Meggie Tazbah, 53, who also holds Bratinas nationality.
			######################
			输出:
			("entity"{tuple_delimiter}FIRUZABAD{tuple_delimiter}LOCATION{tuple_delimiter}Firuzabad held Aurelians as hostages)
			{record_delimiter}
			("entity"{tuple_delimiter}AURELIA{tuple_delimiter}LOCATION{tuple_delimiter}Country seeking to release hostages)
			{record_delimiter}
			("entity"{tuple_delimiter}QUINTARA{tuple_delimiter}LOCATION{tuple_delimiter}Country that negotiated a swap of money in exchange for hostages)
			{record_delimiter}
			{record_delimiter}
			("entity"{tuple_delimiter}TIRUZIA{tuple_delimiter}LOCATION{tuple_delimiter}Capital of Firuzabad where the Aurelians were being held)
			{record_delimiter}
			("entity"{tuple_delimiter}KROHAARA{tuple_delimiter}LOCATION{tuple_delimiter}Capital city in Quintara)
			{record_delimiter}
			("entity"{tuple_delimiter}CASHION{tuple_delimiter}LOCATION{tuple_delimiter}Capital city in Aurelia)
			{record_delimiter}
			("entity"{tuple_delimiter}SAMUEL NAMARA{tuple_delimiter}PERSON{tuple_delimiter}Aurelian who spent time in Tiruzia's Alhamia Prison)
			{record_delimiter}
			("entity"{tuple_delimiter}ALHAMIA PRISON{tuple_delimiter}LOCATION{tuple_delimiter}Prison in Tiruzia)
			{record_delimiter}
			("entity"{tuple_delimiter}DURKE BATAGLANI{tuple_delimiter}PERSON{tuple_delimiter}Aurelian journalist who was held hostage)
			{record_delimiter}
			("entity"{tuple_delimiter}MEGGIE TAZBAH{tuple_delimiter}PERSON{tuple_delimiter}Bratinas national and environmentalist who was held hostage)
			{record_delimiter}
			("relationship"{tuple_delimiter}FIRUZABAD{tuple_delimiter}AURELIA{tuple_delimiter}Firuzabad negotiated a hostage exchange with Aurelia{tuple_delimiter}2)
			{record_delimiter}
			("relationship"{tuple_delimiter}QUINTARA{tuple_delimiter}AURELIA{tuple_delimiter}Quintara brokered the hostage exchange between Firuzabad and Aurelia{tuple_delimiter}2)
			{record_delimiter}
			("relationship"{tuple_delimiter}QUINTARA{tuple_delimiter}FIRUZABAD{tuple_delimiter}Quintara brokered the hostage exchange between Firuzabad and Aurelia{tuple_delimiter}2)
			{record_delimiter}
			("relationship"{tuple_delimiter}SAMUEL NAMARA{tuple_delimiter}ALHAMIA PRISON{tuple_delimiter}Samuel Namara was a prisoner at Alhamia prison{tuple_delimiter}8)
			{record_delimiter}
			("relationship"{tuple_delimiter}SAMUEL NAMARA{tuple_delimiter}MEGGIE TAZBAH{tuple_delimiter}Samuel Namara and Meggie Tazbah were exchanged in the same hostage release{tuple_delimiter}2)
			{record_delimiter}
			("relationship"{tuple_delimiter}SAMUEL NAMARA{tuple_delimiter}DURKE BATAGLANI{tuple_delimiter}Samuel Namara and Durke Bataglani were exchanged in the same hostage release{tuple_delimiter}2)
			{record_delimiter}
			("relationship"{tuple_delimiter}MEGGIE TAZBAH{tuple_delimiter}DURKE BATAGLANI{tuple_delimiter}Meggie Tazbah and Durke Bataglani were exchanged in the same hostage release{tuple_delimiter}2)
			{record_delimiter}
			("relationship"{tuple_delimiter}SAMUEL NAMARA{tuple_delimiter}FIRUZABAD{tuple_delimiter}Samuel Namara was a hostage in Firuzabad{tuple_delimiter}2)
			{record_delimiter}
			("relationship"{tuple_delimiter}MEGGIE TAZBAH{tuple_delimiter}FIRUZABAD{tuple_delimiter}Meggie Tazbah was a hostage in Firuzabad{tuple_delimiter}2)
			{record_delimiter}
			("relationship"{tuple_delimiter}DURKE BATAGLANI{tuple_delimiter}FIRUZABAD{tuple_delimiter}Durke Bataglani was a hostage in Firuzabad{tuple_delimiter}2)
			{completion_delimiter}

			######################
			-真实数据-
			######################
			Entity_types: {entity_types}
			文本: {input_text}
			######################
			输出:
			"""
		.replace("{tuple_delimiter}", GRAPH_TUPLE_DELIMITER)
		.replace("{entity_types}", String.join(",", GRAPH_ENTITY_EXTRACTION_ENTITY_TYPES))
		.replace("{completion_delimiter}", GRAPH_COMPLETION_DELIMITER)
		.replace("{record_delimiter}", GRAPH_RECORD_DELIMITER);

}
