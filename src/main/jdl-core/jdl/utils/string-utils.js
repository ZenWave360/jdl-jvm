/**
 * Copyright 2013-2022 the original author or authors from the JHipster project.
 *
 * This file is part of the JHipster project, see https://www.jhipster.tech/
 * for more information.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
const pluralize = require('pluralize');

module.exports = {
  camelCase,
  kebabCase,
  upperFirst,
  lowerFirst,
  pluralize,
};

function camelCase(string) {
  checkStringIsValid(string);
  if (string === '') {
    return string;
  }
  const [firstLetter, ...rest] = string.replace(/[\W_]/g, '');
  return `${firstLetter.toLowerCase()}${rest.join('')}`;
}

function kebabCase(string) {
  return string
    .replace(/([a-z])([A-Z])/g, '$1-$2')
    .replace(/[\s_]/g, '-')
    .toLowerCase();
}

function upperFirst(string) {
  checkStringIsValid(string);
  if (string === '') {
    return string;
  }
  const [firstLetter, ...rest] = string;
  return `${firstLetter.toUpperCase()}${rest.join('')}`;
}

function lowerFirst(string) {
  checkStringIsValid(string);
  if (string === '') {
    return string;
  }
  const [firstLetter, ...rest] = string;
  return `${firstLetter.toLowerCase()}${rest.join('')}`;
}

function checkStringIsValid(string) {
  if (string === undefined || string === null) {
    throw new Error('The passed string cannot be nil.');
  }
}
