package cc.trity.sun.activities.h5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cc.trity.sun.R;

/**
 * 自定义html文本，并进行加载
 */
public class MoveDetailActivity extends AppCompatActivity {

    @InjectView(R.id.wvAds)
    WebView wvAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_detail);
        ButterKnife.inject(this);

        String template = getFromAssets("data1_template.html");
        StringBuilder sbContent = new StringBuilder();

        ArrayList<MovieInfo> movieList = organizeMovieList();
        for (MovieInfo movie : movieList) {
            String rowData;
            rowData = template.replace("<name>", movie.getName());
            rowData = rowData.replace("<price>", movie.getPrice());
            sbContent.append(rowData);
        }

        String realData = getFromAssets("102.html");
        realData = realData.replace("<data1DefinedByBaobao>",
                sbContent.toString());

        wvAds.loadData(realData, "text/html", "utf-8");
    }

    public ArrayList<MovieInfo> organizeMovieList() {
        ArrayList<MovieInfo> movieList = new ArrayList<>();
        movieList.add(new MovieInfo("Movie 1", "120"));
        movieList.add(new MovieInfo("Movie B", "80"));
        movieList.add(new MovieInfo("Movie III", "60"));

        return movieList;
    }

    public class MovieInfo {
        public MovieInfo(String name, String price) {
            this.name = name;
            this.price = price;
        }

        private String name;
        private String price;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }
    }

    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String Result = "";
            while ((line = bufReader.readLine()) != null)
                Result += line;
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
