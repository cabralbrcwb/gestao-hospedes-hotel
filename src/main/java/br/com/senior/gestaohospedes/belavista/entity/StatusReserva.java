package br.com.senior.gestaohospedes.belavista.entity;

/**
 * Representa os possíveis status de uma reserva no hotel.
 * O fluxo normal é: PENDENTE -> CHECK_IN -> CHECK_OUT.
 */
public enum StatusReserva {
  PENDENTE,      // Reserva criada, aguardando check-in
  CONFIRMADA,    // Status legado, pode ser usado para reservas pagas antecipadamente
  CHECK_IN,      // Hóspede já está no hotel
  CHECK_OUT,     // Hóspede finalizou a estadia
  CANCELADA      // Reserva foi cancelada
}