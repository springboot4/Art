/// *
// * COPYRIGHT (C) 2023 Art AUTHORS(fxzcloud@gmail.com). ALL RIGHTS RESERVED.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package com.art.common.hazelcast.config;
//
// import com.art.common.hazelcast.core.mq.AbstractGroupMessageListener;
// import com.art.common.hazelcast.core.mq.AbstractTopicMessageListener;
// import com.art.common.hazelcast.core.mq.HazelcastMQTemplate;
// import com.art.common.hazelcast.core.mq.HazelcastMessageListenerProvider;
// import com.hazelcast.core.HazelcastInstance;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.boot.autoconfigure.AutoConfiguration;
// import org.springframework.context.annotation.Bean;
//
// import java.util.List;
//
/// **
// * @author Fxz
// * @version 0.0.1
// * @date 2023/3/24 20:42
// */
// @SuppressWarnings("all")
// @AutoConfiguration
// public class HazelcastMessageAutoConfiguration {
//
// @Bean
// HazelcastMQTemplate hazelcastMQTemplate(@Qualifier("hazelcastInstance")
/// HazelcastInstance hazelcastInstance) {
// return new HazelcastMQTemplate(hazelcastInstance);
// }
//
// @Bean
// HazelcastMessageListenerProvider hazelcastMessageListenerProvider(
// @Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance,
// List<AbstractTopicMessageListener<?>> topicMessageListeners,
// List<AbstractGroupMessageListener<?>> groupMessageListeners) {
// HazelcastMessageListenerProvider hazelcastMessageListenerProvider = new
/// HazelcastMessageListenerProvider(
// hazelcastInstance);
// topicMessageListeners.forEach(hazelcastMessageListenerProvider::register);
// groupMessageListeners.forEach(hazelcastMessageListenerProvider::register);
// return hazelcastMessageListenerProvider;
// }
//
// }
