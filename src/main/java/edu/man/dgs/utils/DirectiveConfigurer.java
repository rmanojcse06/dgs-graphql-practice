package edu.man.dgs.utils;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsDirective;
import com.netflix.graphql.dgs.DgsRuntimeWiring;
import graphql.schema.*;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
@Slf4j
@DgsComponent
public class DirectiveConfigurer {

    @DgsRuntimeWiring
    public RuntimeWiring.Builder addUpperDirective(RuntimeWiring.Builder builder) {
        return builder.directive("upper",
                new SchemaDirectiveWiring() {
                    @Override
                    public GraphQLFieldDefinition onField(SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition> env) {
                        GraphQLFieldsContainer fieldsContainer = env.getFieldsContainer();
                        GraphQLFieldDefinition fieldDefinition = env.getFieldDefinition();
                        if (!(fieldsContainer instanceof GraphQLObjectType)) {
                            throw new IllegalArgumentException("Directive 'upper' can only be used on fields within object types.");
                        }

                        GraphQLObjectType parentType = (GraphQLObjectType) fieldsContainer;
                        GraphQLFieldDefinition field = env.getElement();

                        DataFetcher<?> originalDataFetcher = env.getCodeRegistry().getDataFetcher(parentType, field);
                        DataFetcher<?> dataFetcher = DataFetcherFactories.wrapDataFetcher(
                                originalDataFetcher,
                                (dataFetchingEnvironment, value) -> {
                                    if (value instanceof String) {
                                        return ((String) value).toUpperCase();
                                    }
                                    return value;
                                }
                        );

                        env.getCodeRegistry().dataFetcher(parentType, fieldDefinition, dataFetcher);

                        return fieldDefinition;
                    }
                }
        );
    }
}