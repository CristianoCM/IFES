
package bladeofnight.armazenamento.pg;

import bladeofnight.armazenamento.JogadorDAO;
import bladeofnight.entidades.Jogador;
import bladeofnight.entidades.Nave;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class PgJogadorDAO implements JogadorDAO {
    
    private static final String SCRIPT_BUSCAR = "SELECT *" +
                                                "FROM Jogador " +
                                                "WHERE codigo = ?";
    
    private static final String SCRIPT_INSERIR = "INSERT INTO Jogador (nome, nave) VALUES(?, ?)";
    
    private static final String SCRIPT_ALTERAR = "UPDATE Jogador SET nome = ?, nave = ? WHERE codigo = ?";
    
    private static final String SCRIPT_EXCLUIR = "DELETE FROM Jogador WHERE codigo = ?";
    
    private static final String SCRIPT_GETLISTA = "SELECT *" +
                                                  "FROM Jogador ";
    
    private static final String SCRIPT_BUSCARCODIGO_NAVE = "SELECT * FROM Jogador WHERE nave = ?";
    
    @Override
    public Jogador buscar(Jogador jogador) {
        try {
            Connection con = PostgreSqlDAOFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(SCRIPT_BUSCAR);
            ps.setLong(1, jogador.getCodigo());
            // Adquirindo resultado da Query após sua execução
            ResultSet rs = ps.executeQuery();
            // Pulamos para a primeira Linha resultada da Query, se houver alguma coisa, quer dizer que retornamos alguma coisa (não vazio)
            if (rs.next()) {
                long codigoJogador = rs.getLong(1);
                String nome = rs.getString(2);
                long nave = rs.getLong(3);
                
                Jogador jogadorEncontrado = new Jogador(codigoJogador, nave, nome);
                return jogadorEncontrado;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao obter registro de jogador", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return null;
    }
    
    @Override
    public long buscarCodigoComNave(long codNave) {
        try {
            Connection con = PostgreSqlDAOFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(SCRIPT_BUSCARCODIGO_NAVE);
            ps.setLong(1, codNave);
            // Adquirindo resultado da Query após sua execução
            ResultSet rs = ps.executeQuery();
            // Pulamos para a primeira Linha resultada da Query, se houver alguma coisa, quer dizer que retornamos alguma coisa (não vazio)
            if (rs.next()) {
                long codigoJogador = rs.getLong(1);
                return codigoJogador;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao obter o código de um registro de jogador", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return -1;
    }

    @Override
    public boolean inserir(Jogador jogador) {
        try {
            Connection con = PostgreSqlDAOFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(SCRIPT_INSERIR);
            ps.setString(1, jogador.getNomeJogador());
            ps.setLong(2, jogador.getNaveId());

            int resultadoDeLinhasAfetadas = ps.executeUpdate();
            return resultadoDeLinhasAfetadas == 1; // Como acrescenta-se uma linha à tabela, o resultado esperado para sucesso da execução é 1
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao inserir um registro de jogador", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return false;
    }

    @Override
    public boolean alterar(Jogador jogador) {
        try {
            Connection con = PostgreSqlDAOFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(SCRIPT_ALTERAR);
            ps.setString(1, jogador.getNomeJogador());
            ps.setLong(2, jogador.getNaveId());
            ps.setLong(3, jogador.getCodigo());
            
            int resultadoDeLinhasAfetadas = ps.executeUpdate();
            return resultadoDeLinhasAfetadas == 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao alterar um registro de jogador", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return false;
    }

    @Override
    public boolean excluir(Jogador jogador) {
        try {
            Connection con = PostgreSqlDAOFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(SCRIPT_EXCLUIR);
            ps.setLong(1, jogador.getCodigo());
            
            int resultadoDeLinhasAfetadas = ps.executeUpdate();
            return resultadoDeLinhasAfetadas == 1;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao excluir um registro de jogador", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return false;
    }

    private ArrayList<Jogador> listaJogadores;
    
    @Override
    public ArrayList<Jogador> getLista() {
        if (listaJogadores == null) 
            listaJogadores = new ArrayList<>();
        else 
            listaJogadores.clear();
        
        try {
            Connection con = PostgreSqlDAOFactory.getConnection();
            PreparedStatement ps = con.prepareStatement(SCRIPT_GETLISTA);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long codigoJogador = rs.getLong(1);
                String nome = rs.getString(2);
                int nave = rs.getInt(3);
                
                Jogador jog = new Jogador(codigoJogador, nave, nome);
                listaJogadores.add(jog);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocorreu um erro ao obter registros de jogadores", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        
        return listaJogadores;
    }
    
}
