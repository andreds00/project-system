import model.Projeto;
import service.ProjetoService;

public class Main {

    public static void main(String[] args)
        throws Exception {

        ProjetoService service =  new ProjetoService();

        service.carregar();

        System.out.println(
            "Projetos carregados: " + service.listar().size()
        );

        Projeto novoProjeto =
            new Projeto(
                4,
                "Portal de Eventos",
                "Sistema para gerenciamento de eventos",
                "Web",
                "Planejado"
            );

        boolean cadastrado =
            service.adicionar(novoProjeto);

        if (cadastrado) {

            service.salvar();

            System.out.println("Projeto cadastrado e salvo.");

        } else {

            System.out.println("Não foi possível cadastrar.");
        }
    }
}