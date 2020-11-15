package br.com.pi.goodtrip.services;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.pi.goodtrip.models.Viagem;
import br.com.pi.goodtrip.repositories.ViagemRepository;
import br.com.pi.goodtrip.utils.FileUpload;

@Service
public class ViagemService {
		
	@Autowired
	private FileUpload fileUpload;
	
	@Autowired
	private ViagemRepository viagemRepo;
	
	private Optional<String> hasValidDestination(Viagem travel){
		return Optional.of(travel.getDestino())
					   .filter(d -> d.length() > 3)
					   .filter(d -> !d.contains("  "))
					   .filter(d -> d.length() < 20);
	}
	
	private Optional<String> hasValidInit(Viagem travel){
		return Optional.of(travel.getInicio())
				       .filter(ini -> !(ini.length() > 10))
				       .filter(ini -> !(ini.length() < 8))
				       .filter(ini -> !ini.contains(" "));
	}
	
	private Optional<String> hasValidEnd(Viagem travel){
		return Optional.of(travel.getInicio())
				       .filter(ini -> !(ini.length() > 10))
				       .filter(ini -> !(ini.length() < 8))
				       .filter(ini -> !ini.contains(" "));
	}
	
	public Viagem readATravelById(int id) throws NoSuchElementException{
		Viagem foundTravel = 
				viagemRepo.findById(id)
						  .orElseThrow(() -> new NoSuchElementException("Viagem não encontrada"));
		
		return foundTravel;
	}
	

	public List<Viagem> readTravelsBelongToUser(int user, Boolean finalised) throws NoSuchElementException{
		
		List<Viagem> belongsToUser = viagemRepo.selectTravelsByUserIdWhereFinalised(user, finalised);
				
		Optional.of(belongsToUser)
				.filter(list -> !list.isEmpty())
				.orElseThrow(() -> new NoSuchElementException("Viagens de usuário não encontradas"));
		
		return belongsToUser;
	}
	

	public Viagem writeAnTravel(Viagem travel) throws IllegalArgumentException{
		
		hasValidDestination(travel)
				.orElseThrow(() -> new IllegalArgumentException("Destino de viagem inválido"));
		
		hasValidInit(travel)
				.orElseThrow(() -> new IllegalArgumentException("Inicio de viagem inválido"));
		
		hasValidEnd(travel)
				.orElseThrow(() -> new IllegalArgumentException("Término de viagem inválido"));
		
		return viagemRepo.save(travel);
	}
	
	public Viagem uploadTravelImage(int travel, MultipartFile file) throws NoSuchElementException, IOException{
		Viagem toUpdate = 
				viagemRepo.findById(travel)
						  .orElseThrow(() -> new NoSuchElementException("Não foi possível alterar imagem de viagem. Viagem não encontrada"));
		
		String toSaveFilename = 
				Optional.ofNullable(file)
						.map(f -> fileUpload.saveFileTimestampNamed("images", f))
						.orElse("default_travel_image.png");
		
		toUpdate.setImagem(toSaveFilename);
			
		return viagemRepo.save(toUpdate);
	}
	

	public Viagem editATravel( int id,  Viagem data) throws NoSuchElementException, IllegalArgumentException{
		Viagem toUpdate = 
				viagemRepo.findById(id)
				          .orElseThrow(() -> new NoSuchElementException("Não foi possível fazer a edição, viagem não encotrada"));
		
		String verifiedDestination =
				hasValidDestination(data)
						  .orElseThrow(() -> new IllegalArgumentException("Destino de viagem inválido"));
		 
		String verifiedInit =
				hasValidInit(data)
						  .orElseThrow(() -> new IllegalArgumentException("Inicio de viagem inválido"));

		String verifiedEnd =
				hasValidEnd(data)
						  .orElseThrow(() -> new IllegalArgumentException("Término de viagem inválido"));
		
		toUpdate.setDestino(verifiedDestination);
		toUpdate.setInicio(verifiedInit);
		toUpdate.setTermino(verifiedEnd);
		
		return viagemRepo.save(toUpdate);
	}
}