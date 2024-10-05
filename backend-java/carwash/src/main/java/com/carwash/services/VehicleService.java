package com.carwash.services;

import com.carwash.controllers.dtos.VehicleCreateDTO;
import com.carwash.controllers.dtos.VehicleReadDTO;
import com.carwash.entities.Vehicle;
import com.carwash.entities.VehicleInformation;
import com.carwash.exceptions.ResourceNotFoundException;
import com.carwash.exceptions.ResourceStorageException;
import com.carwash.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:MissingJavadocType", "checkstyle:MissingJavadocMethod"})
public class VehicleService {
  @Autowired
  private final VehicleRepository vehicleRepository;

  @Transactional(readOnly = true)
  public VehicleReadDTO findById(Long id) {
    try {
      Vehicle vehicle = vehicleRepository.findById(id)
              .orElseThrow(()
                      -> new ResourceNotFoundException("Veículo não encontrado para o Id = " + id));
      return VehicleReadDTO.getVehicleReadDTO(vehicle);
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  public VehicleReadDTO findByLicensePlate(String plate) {
    try {
      Objects.requireNonNull(plate);
      Optional<Vehicle> vehicle = (vehicleRepository.findByLicensePlate(plate));
      return VehicleReadDTO.getVehicleReadDTO(vehicle.get());
    } catch (NoSuchElementException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @Transactional(readOnly = true)
  public List<VehicleReadDTO> findAll() {
    try {
      List<Vehicle> vehicles = vehicleRepository.findAll();
      if (vehicles.isEmpty()) {
        throw new ResourceNotFoundException("Nenhum veículo encontrado na busca");
      }
      return vehicles.stream().map(VehicleReadDTO::getVehicleReadDTO).collect(Collectors.toList());
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  @Transactional
  public VehicleReadDTO create(VehicleCreateDTO vehicleCreateDto) {
    Vehicle vehicle = getEntityFromDto(vehicleCreateDto);
    try {
      vehicleRepository.save(vehicle);
      return VehicleReadDTO.getVehicleReadDTO(vehicle);
    } catch (ResourceStorageException e) {
      throw new ResponseStatusException(HttpStatus
              .INTERNAL_SERVER_ERROR,
              new ResourceStorageException(String.format("Erro interno ao tentar criar veículo"))
                      .getMessage());
    }
  }

  private Vehicle getEntityFromDto(VehicleCreateDTO vehicleCreateDto) {
    Vehicle vehicle = new Vehicle();
    BeanUtils.copyProperties(vehicleCreateDto, vehicle);
    return vehicle;
  }

  public void deleteVehicle(Long id) {
    try {
      Vehicle vehicleFound = vehicleRepository.findById(id).orElseThrow(() -> {
        throw new ResourceNotFoundException("Veículo não encontrado para o Id = " + id);
      });
      vehicleRepository.deleteById(vehicleFound.getId());
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  public VehicleReadDTO update(Long id, VehicleCreateDTO vehicleCreateDtO) {
    try {
      Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> {
        throw new ResourceNotFoundException("Veículo não encontrado para o Id = " + id);
      });
      BeanUtils.copyProperties(vehicleCreateDtO, vehicle);
      vehicle = vehicleRepository.save(vehicle);
      return VehicleReadDTO.getVehicleReadDTO(vehicle);
    } catch (ResourceNotFoundException e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
    }
  }

  public VehicleInformation findVehicleInformation(String licensePlate) {
    String url = "https://www.keplaca.com/placa?placa-fipe=" + licensePlate;
    try {
      Document document = getDocument(url);
      return getVehicleInformation(document, licensePlate);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static VehicleInformation getVehicleInformation(Document document, String licensePlate) {
    String plate = licensePlate;
    String brand = document.select("td:contains(Marca) + td").text();
    String carModel = document.select("td:contains(Modelo) + td").first().text();
    String color = document.select("td:contains(Cor) + td").text();
    return new VehicleInformation(plate, brand, carModel, color);
  }

  private static Document getDocument(String url) throws IOException {
    Document document = Jsoup.connect(url)
            .userAgent(
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                            "(KHTML, como Gecko) Chrome/91.0.4472.124 Safari/537.36")
            .referrer("https://www.google.com")
            .header("Accept-Language", "en-US,en;q=0.5")
            .header("Connection", "keep-alive")
            .header("Cache-Control", "max-age=0")
            .header("Upgrade-Insecure-Requests", "1")
            .header("Sec-Fetch-Site", "same-origin")
            .header("Sec-Fetch-Mode", "navigate")
            .header("Sec-Fetch-User", "?1")
            .header("Sec-Fetch-Dest", "document")
            .timeout(10000)
            .get();
    return document;
  }
}


