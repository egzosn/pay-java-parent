/*
 * Copyright 2017 the original egan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.egzosn.pay.common.api;

import java.util.Map;

/**
 * 回调，可用于类型转换
 * @author: egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2017/3/7 18:55
 *  </pre>
 */
public interface Callback<T> {
     /**
      * 执行者
      * @param map 需要转化的map
      * @return 处理过后的类型对象
      */
     T perform(Map<String, Object> map);

}
