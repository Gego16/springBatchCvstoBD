package com.api.web.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.api.web.model.Estudiante;

@Configuration
@EnableBatchProcessing
public class EstudianteBatchConfig {
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private DataSource dataSource;
	
	@Bean
	public FlatFileItemReader<Estudiante>readFromCsv(){
		FlatFileItemReader<Estudiante> reader = new FlatFileItemReader<Estudiante>();
		reader.setResource(new FileSystemResource("C://Users/PLANETMEDIA/Desktop/Capacitación/SpringBatch1.csv"));
		// reader.setResource(new ClassPathResource("csv_input.scv"));
		reader.setLineMapper(new DefaultLineMapper<Estudiante>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
				{
					setNames(Estudiante.fields());
				}
			  });
				setFieldSetMapper(new BeanWrapperFieldSetMapper<Estudiante>() {
					
					{
						setTargetType(Estudiante.class);
					}
				});
			}
		});
		return reader;
	}
	
	@Bean
	public JdbcBatchItemWriter<Estudiante> writerIntoDB(){
		JdbcBatchItemWriter<Estudiante> writer = new JdbcBatchItemWriter<Estudiante>();
		writer.setDataSource(dataSource);
		writer.setSql("insert into cvsdata (id,nombre,apPaterno,apMaterno,correo,edad) values (:id,:nombre,:apPaterno,:apMaterno,:correo,:edad)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Estudiante>());
		return writer;
	}
	@Bean
	public Step step() {
		return stepBuilderFactory.get("step3").<Estudiante,Estudiante>chunk(10)
		.reader(readFromCsv()).writer(writerIntoDB()).build();
	}
	@Bean
	public Job job() {
		return jobBuilderFactory.get("job").flow(step()).end().build();
	}
}
	