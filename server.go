package main
import (
	"database/sql"
	_ "github.com/go-sql-driver/mysql"
//    "fmt"
	"time"
	"os"
    "net/http"
    "github.com/gin-gonic/gin"
)

var g_ch_codes = make(chan string, 10)
var db *sql.DB
var err error 

func dbInsCode(dbusr string, dbpass string, dbnm string) {

	var con_str string
	con_str = dbusr + ":" + dbpass + "@/" + dbnm

	db, _ := sql.Open("mysql", con_str)
	defer db.Close()

	for {
        select {
        case code := <-g_ch_codes:
			// Execute the query
			_, err := db.Query("INSERT INTO codes (code) VALUES (?);", code)
			if err != nil {
				panic(err.Error()) // proper error handling instead of panic in your app
			}            
        default:
		}
		time.Sleep(time.Second)
	}
}

func webServer() {
	r := gin.Default()
    r.GET("/add-barcode/:code", func(c *gin.Context) {
		var p_code string
		p_code = c.Param("code")
		g_ch_codes <- p_code
		
		c.JSON(http.StatusOK, gin.H{
			"err" : "none",
		})
    })
    r.Run()
}

func main() {
	var dbusr string
	var dbpass string
	var dbnm string

	dbusr = os.Getenv("DB_USER")
	dbpass = os.Getenv("DB_PASS")
	dbnm = os.Getenv("DB_DTBS")

	go dbInsCode(dbusr, dbpass, dbnm)
	go webServer()

	for {time.Sleep(time.Second)}
}
