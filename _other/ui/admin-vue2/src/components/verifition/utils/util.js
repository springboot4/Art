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

export function resetSize(vm) {
  var img_width, img_height, bar_width, bar_height	// 图片的宽度、高度，移动条的宽度、高度

  var parentWidth = vm.$el.parentNode.offsetWidth || window.offsetWidth
  var parentHeight = vm.$el.parentNode.offsetHeight || window.offsetHeight

  if (vm.imgSize.width.indexOf('%') != -1) {
    img_width = parseInt(this.imgSize.width) / 100 * parentWidth + 'px'
  } else {
    img_width = this.imgSize.width
  }

  if (vm.imgSize.height.indexOf('%') != -1) {
    img_height = parseInt(this.imgSize.height) / 100 * parentHeight + 'px'
  } else {
    img_height = this.imgSize.height
  }

  if (vm.barSize.width.indexOf('%') != -1) {
    bar_width = parseInt(this.barSize.width) / 100 * parentWidth + 'px'
  } else {
    bar_width = this.barSize.width
  }

  if (vm.barSize.height.indexOf('%') != -1) {
    bar_height = parseInt(this.barSize.height) / 100 * parentHeight + 'px'
  } else {
    bar_height = this.barSize.height
  }

  return { imgWidth: img_width, imgHeight: img_height, barWidth: bar_width, barHeight: bar_height }
}

export const _code_chars = [1, 2, 3, 4, 5, 6, 7, 8, 9, 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z']
export const _code_color1 = ['#fffff0', '#f0ffff', '#f0fff0', '#fff0f0']
export const _code_color2 = ['#FF0033', '#006699', '#993366', '#FF9900', '#66CC66', '#FF33CC']
