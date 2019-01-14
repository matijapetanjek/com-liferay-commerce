/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.openapi.util.generator;

import com.liferay.commerce.openapi.util.ComponentDefinition;
import com.liferay.commerce.openapi.util.Content;
import com.liferay.commerce.openapi.util.Method;
import com.liferay.commerce.openapi.util.Parameter;
import com.liferay.commerce.openapi.util.Path;
import com.liferay.commerce.openapi.util.Response;
import com.liferay.commerce.openapi.util.Schema;
import com.liferay.commerce.openapi.util.util.StringUtils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author Igor Beslic
 */
public class ResourceGenerator {

	public String generateResourceGetters(List<Path> paths) {
		Iterator<Path> iterator = paths.iterator();

		StringBuilder sb = new StringBuilder();

		while (iterator.hasNext()) {
			Path path = iterator.next();

			sb.append("\t@Path(\"");
			sb.append(path.getPath());
			sb.append("\")\n");
			sb.append("\tpublic Object get");
			sb.append(StringUtils.upperCaseFirstChar(path.getName()));
			sb.append("() {\n");
			sb.append("\t\treturn _");
			sb.append(path.getName());
			sb.append("Resource;\n");
			sb.append("\t}\n");

			if (iterator.hasNext()) {
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	public String generateResourceImports(
		String resourcePackage, List<Path> paths) {

		Iterator<Path> iterator = paths.iterator();

		StringBuilder sb = new StringBuilder();

		while (iterator.hasNext()) {
			Path path = iterator.next();

			sb.append("import ");
			sb.append(resourcePackage);
			sb.append(".");
			sb.append(StringUtils.upperCaseFirstChar(path.getName()));
			sb.append("Resource;\n");

			if (iterator.hasNext()) {
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	public String generateResourceReferences(List<Path> paths) {
		Iterator<Path> iterator = paths.iterator();

		StringBuilder sb = new StringBuilder();

		while (iterator.hasNext()) {
			Path path = iterator.next();

			sb.append("\t@Reference\n");
			sb.append("\tprivate ");
			sb.append(StringUtils.upperCaseFirstChar(path.getName()));
			sb.append("Resource _");
			sb.append(path.getName());
			sb.append("Resource;");

			if (iterator.hasNext()) {
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	public String toJavaxImports(List<Method> methods) {
		StringBuilder sb = new StringBuilder();

		Set<String> importedClasses = new HashSet<>();

		for (Method method : methods) {
			if (!importedClasses.contains(method.getHttpMethod())) {
				sb.append("import javax.ws.rs.");
				sb.append(method.getHttpMethod());
				sb.append(";\n");

				importedClasses.add(method.getHttpMethod());
			}

			if ((method.getAccepts() != null) &&
				!importedClasses.contains("Consumes")) {

				sb.append("import javax.ws.rs.Consumes;\n");

				importedClasses.add("Consumes");
			}

			if (!importedClasses.contains("Produces")) {
				for (Response response : method.getResponses()) {
					if (response.hasContent()) {
						sb.append("import javax.ws.rs.Produces;\n");

						importedClasses.add("Produces");

						break;
					}
				}
			}

			for (Parameter parameter : method.getParameters()) {
				String location = parameter.getLocation();

				if (location.equals("body")) {
					continue;
				}

				if (importedClasses.contains(location)) {
					continue;
				}

				sb.append("import javax.ws.rs.");
				sb.append(StringUtils.upperCaseFirstChar(location));
				sb.append("Param;\n");

				importedClasses.add(location);
			}
		}

		return sb.toString();
	}

	public String toModelImportStatements(
		String modelPackage, Set<String> referencedModels) {

		StringBuilder sb = new StringBuilder();

		for (String referencedModel : referencedModels) {
			sb.append("import ");
			sb.append(modelPackage);
			sb.append(".");
			sb.append(StringUtils.upperCaseFirstChar(referencedModel));
			sb.append("DTO;\n");
		}

		return sb.toString();
	}

	public String toResourceImplementationMethods(
		List<Method> methods, Set<ComponentDefinition> schemaComponents) {

		StringBuilder sb = new StringBuilder();

		Iterator<Method> iterator = methods.iterator();

		while (iterator.hasNext()) {
			Method method = iterator.next();

			sb.append("\t@Override\n");

			_appendMethodDeclaration(sb, method, false, schemaComponents);

			sb.append(" {\n");

			if (method.hasResponseContent()) {
				ComponentDefinition schemaComponent = _getSchemaComponent(
					method, schemaComponents);

				if (Objects.equals(schemaComponent.getType(), "array")) {
					sb.append("\t\treturn Collections.emptyList();\n");
				}
				else {
					sb.append("\t\treturn null;\n");
				}
			}
			else {
				sb.append("\t\treturn Response.ok().build();\n");
			}

			sb.append("\t}\n");

			if (iterator.hasNext()) {
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	public String toResourceInterfaceMethods(
		List<Method> methods, Set<ComponentDefinition> schemaComponents) {

		StringBuilder sb = new StringBuilder();

		Iterator<Method> iterator = methods.iterator();

		while (iterator.hasNext()) {
			Method method = iterator.next();

			sb.append("\t@Path(\"");
			sb.append(method.getPath());
			sb.append("\")\n");
			sb.append("\t@");
			sb.append(method.getHttpMethod());
			sb.append("\n");

			for (Response response : method.getResponses()) {
				if (response.hasContent()) {
					sb.append("\t@Produces(\"");

					Content content = response.getContent();

					sb.append(content.getMimeType());

					sb.append("\")\n");
				}
			}

			if (method.getAccepts() != null) {
				sb.append("\t@Consumes(\"");
				sb.append(method.getAccepts());
				sb.append("\")\n");
			}

			_appendMethodDeclaration(sb, method, true, schemaComponents);

			sb.append(";\n");

			if (iterator.hasNext()) {
				sb.append("\n");
			}
		}

		return sb.toString();
	}

	private void _appendMethodDeclaration(
		StringBuilder sb, Method method, boolean annotateParameter,
		Set<ComponentDefinition> schemaComponents) {

		sb.append("\tpublic ");

		_appendReturnValue(sb, method, schemaComponents);

		sb.append(method.getName());

		sb.append("(");

		List<Parameter> parameters = method.getParameters();

		Iterator<Parameter> parameterIterator = parameters.iterator();

		while (parameterIterator.hasNext()) {
			if (annotateParameter) {
				sb.append(
					_parameterGenerator.toAnnotatedMethodParameter(
						parameterIterator.next()));
			}
			else {
				sb.append(
					_parameterGenerator.toMethodParameter(
						parameterIterator.next()));
			}

			if (parameterIterator.hasNext()) {
				sb.append(", ");
			}
		}

		sb.append(")");
	}

	private void _appendReturnValue(
		StringBuilder sb, Method method,
		Set<ComponentDefinition> schemaComponents) {

		if (method.hasResponseContent()) {
			ComponentDefinition schemaComponent = _getSchemaComponent(
				method, schemaComponents);

			String itemsReferenceModel =
				schemaComponent.getItemsReferencedModel();

			if (Objects.equals(schemaComponent.getType(), "array") &&
				(itemsReferenceModel != null)) {

				schemaComponent = _getSchemaComponent(
					itemsReferenceModel, schemaComponents);

				sb.append("CollectionDTO<");
				sb.append(schemaComponent.getName());
				sb.append("DTO> ");
			}
			else if (Objects.equals(schemaComponent.getType(), "object")) {
				sb.append(schemaComponent.getName());
				sb.append("DTO ");
			}
			else {
				sb.append("Response ");
			}
		}
		else {
			sb.append("Response ");
		}
	}

	private Content _getResponseContent(List<Response> responses) {
		Stream<Response> responseStream = responses.stream();

		return responseStream.filter(
			response -> response.getContent() != null
		).findFirst(
		).map(
			Response::getContent
		).orElse(
			null
		);
	}

	private ComponentDefinition _getSchemaComponent(
		Method method, Set<ComponentDefinition> schemaComponents) {

		Content content = _getResponseContent(method.getResponses());

		Schema schema = content.getSchema();

		return _getSchemaComponent(
			schema.getReferencedModel(), schemaComponents);
	}

	private ComponentDefinition _getSchemaComponent(
		String name, Set<ComponentDefinition> schemaComponents) {

		Stream<ComponentDefinition> componentDefinitionStream =
			schemaComponents.stream();

		return componentDefinitionStream.filter(
			componentDefinition ->
				Objects.equals(name, componentDefinition.getName())
		).findFirst(
		).orElse(
			null
		);
	}

	private final ParameterGenerator _parameterGenerator =
		new ParameterGenerator();

}