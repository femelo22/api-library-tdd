package br.com.lfmelo;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling //habilitar o agendamento de tarefas
public class ApiLibraryApplication {

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Scheduled(cron = "0 53 9 1/1 * ?")
	public void testeAgendamentoTarefas() {
		System.out.println("DISPARANDO TAREFA...");
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiLibraryApplication.class, args);
	}

}
