package edu.man.dgs.utils;

import com.netflix.graphql.dgs.DgsScalar;
import graphql.GraphQLContext;
import graphql.execution.CoercedVariables;
import graphql.language.StringValue;
import graphql.language.Value;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@DgsScalar(name = "DateTime")
public class DateTimeScalar implements Coercing<LocalDateTime, String> {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public @Nullable String serialize(@NotNull Object dataFetcherResult, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingSerializeException {
        log.info("Inside DateTimeScalar.serialize() ==> dataFetcherResult: {}, graphQLContext: {}, locale: {}", dataFetcherResult, graphQLContext, locale);
        if (dataFetcherResult == null) {
            return null;
        }
        if (dataFetcherResult instanceof LocalDateTime) {
            return ((LocalDateTime) dataFetcherResult).format(DATE_FORMATTER);
        }
        return Coercing.super.serialize(dataFetcherResult, graphQLContext, locale);
    }

    @Override
    public @Nullable LocalDateTime parseValue(@NotNull Object input, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingParseValueException {
        log.info("Inside DateTimeScalar.parseValue() ==> input: {}, graphQLContext: {}, locale: {}", input, graphQLContext, locale);
        if (input instanceof String) {
            try {
                return LocalDateTime.parse((String) input, DATE_FORMATTER);
            } catch (Exception e) {
                throw new CoercingParseValueException("Invalid date format: " + input);
            }
        }
        return Coercing.super.parseValue(input, graphQLContext, locale);
    }

    @Override
    public @Nullable LocalDateTime parseLiteral(@NotNull Value<?> input, @NotNull CoercedVariables variables, @NotNull GraphQLContext graphQLContext, @NotNull Locale locale) throws CoercingParseLiteralException {
        log.info("Inside DateTimeScalar.parseLiteral() ==> input: {}, variables: {}, graphQLContext: {}, locale: {}", input, variables, graphQLContext, locale);
        if (input instanceof StringValue) {
            try {
                return LocalDateTime.parse(((StringValue) input).getValue(), DATE_FORMATTER);
            } catch (Exception e) {
                throw new CoercingParseValueException("Invalid date format: " + input);
            }
        }
        return Coercing.super.parseLiteral(input, variables, graphQLContext, locale);
    }
}
