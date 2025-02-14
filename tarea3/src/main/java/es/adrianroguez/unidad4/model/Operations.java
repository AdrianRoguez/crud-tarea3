package es.adrianroguez.unidad4.model;

import java.util.Map;

public interface Operations {
    public boolean create(Empleado empleado);

    public Empleado read(String identificador);

    public Empleado read(Empleado empleado);

    public boolean update(Empleado empleado);

    public boolean delete(String identificador);

    public Map<String, Empleado> empleadosPorPuesto(String puesto);

    public Map<String, Empleado> empleadosPorEdad(String fechaInicio, String fechaFin);
}
