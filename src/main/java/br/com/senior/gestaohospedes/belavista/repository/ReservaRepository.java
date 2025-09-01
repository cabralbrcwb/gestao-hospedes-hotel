package br.com.senior.gestaohospedes.belavista.repository;

import br.com.senior.gestaohospedes.belavista.entity.Reserva;
import br.com.senior.gestaohospedes.belavista.entity.StatusReserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByStatus(StatusReserva status);

    Optional<Reserva> findByHospedeIdAndStatus(Long hospedeId, StatusReserva status);

    boolean existsByHospedeIdAndStatusIn(Long hospedeId, List<StatusReserva> statuses);

    List<Reserva> findAllByHospedeIdAndStatusIn(Long hospedeId, List<StatusReserva> statuses);
}
