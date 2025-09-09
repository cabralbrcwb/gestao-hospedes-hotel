package br.com.senior.gestaohospedes.belavista.repository;

import br.com.senior.gestaohospedes.belavista.entity.Reserva;
import br.com.senior.gestaohospedes.belavista.entity.StatusReserva;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<Reserva, Long> {

  List<Reserva> findByStatus(StatusReserva status);

  List<Reserva> findAllByStatusIn(List<StatusReserva> statuses);

  boolean existsByHospedeIdAndStatusIn(Long id, List<StatusReserva> pendente);
}
