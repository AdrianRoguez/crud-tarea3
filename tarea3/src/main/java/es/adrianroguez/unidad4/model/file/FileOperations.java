package es.adrianroguez.unidad4.model.file;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;

import es.adrianroguez.unidad4.model.Empleado;
import es.adrianroguez.unidad4.model.Operations;

public class FileOperations extends FileOperationsMap implements Operations {
    File file;
    String fileName = "archivo.txt";

    public FileOperations() {
        try {
            URL source = getClass().getClassLoader().getResource(fileName);
            file = new File(source.toURI());
            if (!file.exists() || !file.isFile()) {
                throw new IllegalArgumentException("El recurso no es de tipo fichero " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean create(Empleado empleado) {
        if (empleado == null || empleado.getIdentificador() == null || empleado.getIdentificador().isEmpty()) {
            return false;
        }
        Map<String, Empleado> empleados = readFile(file);
        if (empleados.containsValue(empleado)) {
            return false;
        }
        empleados.putIfAbsent(empleado.getIdentificador(), empleado);
        return updateFile(empleados, file);
    }

    @Override
    public Empleado read(String identificador) {
        if (identificador == null || identificador.isEmpty()) {
            return null;
        }
        Map<String, Empleado> empleados = readFile(file);
        return empleados.get(identificador);
    }

    @Override
    public Empleado read(Empleado empleado) {
        if (empleado == null || empleado.getIdentificador() == null || empleado.getIdentificador().isEmpty()) {
            return null;
        }
        return read(empleado.getIdentificador());
    }

    @Override
    public boolean update(Empleado empleado) {
        if (empleado == null || empleado.getIdentificador() == null || empleado.getIdentificador().isEmpty()) {
            return false;
        }
        Map<String, Empleado> empleados = readFile(file);
        if (!empleados.containsKey(empleado.getIdentificador())) {
            return false;
        }
        if (empleados.replace(empleado.getIdentificador(), empleado) != null) {
            return updateFile(empleados, file);
        }
        return updateFile(empleados, file);
    }

    @Override
    public boolean delete(String identificador) {
        if (identificador == null || identificador.isEmpty()) {
            return false;
        }
        Map<String, Empleado> empleados = readFile(file);
        if (!empleados.containsKey(identificador)) {
            return false;
        }
        empleados.remove(identificador);
        return updateFile(empleados, file);
    }

    @Override
    public Map<String, Empleado> empleadosPorPuesto(String puesto) {
        if (puesto == null) {
            return null;
        }
        Map<String, Empleado> empleados = new TreeMap<>();
        Iterator<Empleado> iterator = empleados.values().iterator();
        while (iterator.hasNext()) {
            Empleado empleadoBuscado = iterator.next();
            if (!empleadoBuscado.getPuesto().equals(puesto)) {
                iterator.remove();
            }
        }
        return empleados;
    }

    @Override
    public Map<String, Empleado> empleadosPorEdad(String fechaInicio, String fechaFin) {
         String fechaInicial = fechaInicio;
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate inicio = LocalDate.parse(fechaInicial, formato);
        String fechaFinal = fechaFin;
        LocalDate fin = LocalDate.parse(fechaFinal, formato);
        Map<String, Empleado> empleados = readFile(file);
        if (empleados == null) {
            return null;
        }
        Iterator<Empleado> iterator = empleados.values().iterator();
        while (iterator.hasNext()) {
            Empleado empleadoBuscado = iterator.next();
            LocalDate fechaBuscada = empleadoBuscado.getFechaNacimientoDate();
            if (fechaBuscada.isAfter(inicio) || fechaBuscada.isBefore(fin)) {
                iterator.remove();
            }
        }
        return empleados;
    }
}
