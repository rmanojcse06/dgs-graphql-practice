package edu.man.dgs.utils;

import com.netflix.graphql.dgs.DgsDirective;
import graphql.schema.*;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;

@DgsDirective(name = "lower")
public class LowerCaseDirective implements SchemaDirectiveWiring {
    @Override
    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
        GraphQLFieldsContainer fieldsContainer = env.getFieldsContainer();
        GraphQLFieldDefinition fieldDefinition = env.getFieldDefinition();
        if (!(fieldsContainer instanceof GraphQLObjectType)) {
            throw new IllegalArgumentException("Directive 'lower' can only be used on fields within object types.");
        }

        GraphQLObjectType parentType = (GraphQLObjectType) fieldsContainer;
        GraphQLFieldDefinition field = env.getElement();

        DataFetcher<?> originalDataFetcher = env.getCodeRegistry().getDataFetcher(parentType, field);
        DataFetcher<?> dataFetcher = DataFetcherFactories.wrapDataFetcher(
                originalDataFetcher,
                (dataFetchingEnvironment, value) -> {
                    if (value instanceof String) {
                        return ((String) value).toLowerCase();
                    }
                    return value;
                }
        );

        env.getCodeRegistry().dataFetcher(parentType, fieldDefinition, dataFetcher);

        return fieldDefinition;
    }
}
