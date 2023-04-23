const ejs = require('ejs');
const { parseFromContent } = require('../jdl/readers/jdl-reader');
const ParsedJDLToJDLObjectConverter = require('../jdl/converters/parsed-jdl-to-jdl-object/parsed-jdl-to-jdl-object-converter');
const JDLWithApplicationValidator = require('../jdl/validators/jdl-with-application-validator');
const JDLWithoutApplicationValidator = require('../jdl/validators/jdl-without-application-validator');

module.exports = {
  parseJDL,
  jdlToMermaid,
};

function parseJDL(jdlString, configuration = {}) {
  configuration = { databaseType: 'sql', unidirectionalRelationships: true, ...configuration }; // warning: options may be a java HashMap, we need this transformation
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

const template = `classDiagram
<%_ if (configuration.mermaid?.includeServices) { _%>
  <%_for(const service of Object.values(jdl.options.options.service || {})) { _%>

class <%= service.value %> {
  <<Service>>
  <%_for(const entityName of service.entityNames) { _%>
  +manages(<%= entityName %>)
  <%_} _%>
}
    <%_for(const entityName of service.entityNames) { _%>
<%= service.value %> ..|> <%= entityName %>: manages
    <%_} _%>
  <%_ } _%>
<%_ } _%>


<%_for(const entity of Object.values(jdl.entities)) { _%>

class <%= entity.name %> {
  <<Entity>>
  <%_for(const field of Object.values(entity.fields)) { _%>
  +<%= field.type %><%- field.isArray? '[]' : '' %> <%= field.name %>
  <%_} _%>
}
  <%_for(const field of Object.values(entity.fields)) { _%>
    <%_ if (field.isEnum || field.isEntity) { _%>
<%= field.type %> <%- field.isArray? '"many"' : '' %> *-- <%= entity.name %>: <%= field.name %>
    <%_ } _%>
  <%_ } _%>
<%_ } _%>

<%_for(const _enum of jdl.enums.enums.values()) { _%>

class <%= _enum.name %> {
  <<enumeration>>
    <%_for(const enumValue of _enum.values.values()) { _%>
  <%= enumValue.name %>
    <%_} _%>
}
<%_ } _%>

<%_for(const oneToMany of jdl.relationships.relationships.OneToMany.values()) { _%>
<%= oneToMany.from %> ..o "*" <%= oneToMany.to %> : <%= oneToMany.injectedFieldInFrom %>
<%= oneToMany.from %>: +<%= oneToMany.to %>[] <%= oneToMany.injectedFieldInFrom %>
<%_ } _%>

<%_for(const oneToOne of jdl.relationships.relationships.OneToOne.values()) { _%>
<%= oneToOne.from %> ..o "1" <%= oneToOne.to %> : <%= oneToOne.injectedFieldInFrom %>
<%= oneToOne.from %>: +<%= oneToOne.to %> <%= oneToOne.injectedFieldInFrom %>
<%_ } _%>

<%_for(const manyToOne of jdl.relationships.relationships.ManyToOne.values()) { _%>
<%= manyToOne.from %> "*" ..o <%= manyToOne.to %> : <%= manyToOne.injectedFieldInFrom %>
<%= manyToOne.from %>: +<%= manyToOne.to %> <%= manyToOne.injectedFieldInFrom %>
<%_ } _%>

<%_for(const manyToMany of jdl.relationships.relationships.ManyToMany.values()) { _%>
<%= manyToMany.from %> "*..*" ..o <%= manyToMany.to %> : <%= manyToMany.injectedFieldInFrom %>
<%= manyToMany.from %>: +<%= manyToMany.to %> <%= manyToMany.injectedFieldInFrom %>
<%= manyToMany.to %>: +<%= manyToMany.from %> <%= manyToMany.injectedFieldInTo %>
<%_ } _%>
`;

function jdlToMermaid(jdlString, configuration = {}) {
  const jdl = parseJDL(jdlString, configuration);
  configuration = { mermaid: { includeServices: true }, ...configuration }; // warning: options may be a java HashMap, we need this transformation
  return ejs.render(template, { jdl, configuration });
}
