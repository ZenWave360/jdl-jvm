const { parseFromContent } = require('../jdl/readers/jdl-reader');
const ParsedJDLToJDLObjectConverter = require('../jdl/converters/parsed-jdl-to-jdl-object/parsed-jdl-to-jdl-object-converter');
const JDLWithApplicationValidator = require('../jdl/validators/jdl-with-application-validator');
const JDLWithoutApplicationValidator = require('../jdl/validators/jdl-without-application-validator');

module.exports = {
  parseJDL,
};

function parseJDL(jdlString, configuration = {}) {
  configuration = { databaseType: 'sql', ...configuration }; // warning: options may be a java HashMap, we need this transformation
  const content = parseFromContent(jdlString);
  const jdlObject = getJDLObject(content, configuration);
  checkForErrors(jdlObject, configuration);
  return jdlObject;
}

function getJDLObject(parsedJDLContent, configuration) {
  return ParsedJDLToJDLObjectConverter.parseFromConfigurationObject({
    ...configuration,
    parsedContent: parsedJDLContent,
  });
}

function checkForErrors(jdlObject, configuration, logger = console) {
  let validator;
  const unidirectionalRelationships = null;
  if (jdlObject.getApplicationQuantity() === 0) {
    validator = JDLWithoutApplicationValidator.createValidator(jdlObject, configuration, logger, { unidirectionalRelationships });
  } else {
    validator = JDLWithApplicationValidator.createValidator(jdlObject, logger, { unidirectionalRelationships });
  }
  validator.checkForErrors();
}

// eslint-disable-next-line import/order
// const jdlString = require('fs').readFileSync(process.argv[2], 'utf8');

// parseJDL(jdlString);
