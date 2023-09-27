import java.lang.ProcessBuilder.Redirect;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MY_ShapeMultiThread extends Application{
    public static void main(String[] args){
        launch(args);
    }
    private Canvas MY_Canvas;
    private volatile boolean MY_running;
    private MY_Runner MY_runner;
    private Button MY_StartButton;

    public void start(Stage stage){
        MY_Canvas = new Canvas(640, 480);
        MY_redraw();
        MY_StartButton = new Button("Start!");
        MY_StartButton.setOnAction(e -> MY_doStartOrStop());

        HBox MY_Button = new HBox(MY_StartButton);
        MY_Button.setStyle("-fx-padding: 6px; -fx-border-color:black; -fx-border-width: 3px 0 0 0 ");
        MY_Button.setAlignment(Pos.CENTER);
        BorderPane MY_root = new BorderPane(MY_Canvas);
        MY_root.setBottom(MY_Button);
        Scene MY_Scene = new Scene(MY_root);
        stage.setScene(MY_Scene);
        stage.setTitle("Click start to make random Art");
        stage.setResizable(false);
        stage.show();
    }
    private class MY_Runner extends Thread{
        public void run(){
            while(MY_running){
                Platform.runLater(() -> MY_redraw());
                    try{
                        Thread.sleep(2000);
                    }
                    catch(InterruptedException e){

                    }
            }
        }
    }
    private void MY_redraw(){
        GraphicsContext MY_g = MY_Canvas.getGraphicsContext2D();
        double MY_weight = MY_Canvas.getWidth();
        double MY_height = MY_Canvas.getHeight();

        if(!MY_running){
            MY_g.setFill(Color.WHITE);
            MY_g.fillRect(0, 0, MY_weight, MY_height);
            return;
        }

        Color MY_randomGray = Color.hsb(1, 0, Math.random());
        MY_g.setFill(MY_randomGray);
        MY_g.fillRect(0, 0, MY_weight, MY_height);

        int MY_artType = (int)(3*Math.random());

        switch(MY_artType){
            case 0:
            MY_g.setLineWidth(2);
            for(int i=0;i<500;i++){
                int x1 = (int)(MY_weight*Math.random());
                int y1 = (int)(MY_weight*Math.random());
                int x2 = (int)(MY_weight*Math.random());
                int y2 = (int)(MY_weight*Math.random());
                Color randomHue = Color.hsb(360*Math.random(),1, 1);
                MY_g.setStroke(randomHue);
                MY_g.strokeLine(x1, y1, x2, y2);
            }
            break;
            case 1:
            for(int i=0;i<200;i++){
                int MY_CenterX = (int)(MY_weight*Math.random());
                int MY_CenterY = (int)(MY_height*Math.random());
                Color MY_randomHue = Color.hsb(360*Math.random(), 1, 1);
                MY_g.setStroke(MY_randomGray);
                MY_g.strokeOval(MY_CenterX - 50, MY_CenterY - 50, 10,100);
            }
            break;

            default:
            MY_g.setStroke(Color.BLACK);
            MY_g.setLineWidth(4);
            for(int i=0;i<25;i++){
                int CenterX = (int)(MY_weight*Math.random());
                int CenterY = (int)(MY_weight*Math.random());
                int size = 30+(int)(170*Math.random());
               Color randomColor= Color.color(Math.random(),Math.random(), Math.random());
               MY_g.setFill(randomColor);
                MY_g.fillRect(CenterX - size/2, CenterY - size/2, size, size);
              // MY_g.strokeLine(CenterX - size/2, CenterY - size/2, size, size);
               MY_g.strokeRect(CenterX - size/2, CenterY - size/2, size, size);
            }
            break;
        }
    }
    private void MY_doStartOrStop(){
        if(MY_running==false){
            MY_StartButton.setText("Stop");
            MY_runner = new MY_Runner();
            MY_running = true;
            MY_runner.start();
        }
        else{
            MY_StartButton.setDisable(true);
            MY_running = false;
            MY_redraw();
            MY_runner.interrupt();
            try{
                MY_runner.join(1000);
            }catch(InterruptedException e){


            }

            MY_runner = null;
            MY_StartButton.setText("Start");
            MY_StartButton.setDisable(false);
        }
    }
}