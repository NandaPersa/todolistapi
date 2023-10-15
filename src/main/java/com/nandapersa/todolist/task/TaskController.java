package com.nandapersa.todolist.task;

import com.nandapersa.todolist.utils.Utils;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController //identifica que é um controller
@RequestMapping("/tasks") //nomeia o caminho para ele
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, ServletRequest request){
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        var currentDate = LocalDateTime.now();

        if(currentDate.isAfter(taskModel.getStartAt())){
            return ResponseEntity.status(400).body("A data de início deve ser maior do que a data atual");
        }

        if(taskModel.getEndAt().isBefore(taskModel.getStartAt())){
            return ResponseEntity.status(400).body("A data de términbo deve ser maior do que a data de início");
        }

        var taskCreated = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(taskCreated);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        return this.taskRepository.findByIdUser((UUID) idUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id){
        var task = this.taskRepository.findById(id).orElse(null);

        //Tarefa não existe
        if(task == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tarefa não encontrada");
        }

        var idUser = request.getAttribute("idUser");

        //Verificando se a tarefa perntence aquele usuário
        if(!task.getIdUser().equals(idUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Usuário não tem permissão para alterar essa tarefa");
        }

        Utils.copyNonNullProperties(taskModel, task);

        var taskUpdated = this.taskRepository.save(task);

        return ResponseEntity.ok().body(taskUpdated);
    }

}
