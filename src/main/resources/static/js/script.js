console.log("this is script file")

/*JQuery is used so JQuery and Javascript link must be pasted in base.html from bootstrap*/


/* Sidebar script : Sidebar html code is in normal/base.html that will show user_dashboard content*/

const toggleSidebar = () => {

    if($(".sidebar").is(":visible")) {              // JQuery ka use karke sidebar ko target kiya ja raha
        //true...
        //dikh raha hai to band karna hai

        $(".sidebar").css("display", "none");       // isse sidebar nahi dikhega
        $(".content").css("margin-left","0%");      //  jab sidebar hata diye to content ko bhi left shift kara diye kyuki usme margin-left laga hai

    } else {
        //false
        //agar nahi dikh raha to show karna hai

        $(".sidebar").css("display", "block");
        $(".content").css("margin-left","20%");
    }
};


        // Search Script 



            //      its html code is present in show_contacts.html and backend code is in SearchController.java

const search =() => {
   // console.log("searching. . .");

   let query=$("#search-input").val();    //input box ki id     //input box me jo bhi value aayga uske id se store karaya ja raha query variable me
   
    
   // showing search result............... (show karane ke liye div me class="search-result" ka use)

    if (query == "") {

        $(".search-result").hide();     // matlab query box me kuch nahi rahega, blank rahega to result hide rahega

    } else {
        //search. . .
        console.log(query); 

        //sending request to server (means to backend in SearchController.java)

        let url = `http://localhost:8081/search/${query}`;          //see GetMapping of SearchController.java

        fetch(url)                                                  // fetch promise return karta hai to usko manage karne ke liye then lagaya hai aur usme response ko recieve karaya hai function bana kar
            .then((response) => {
                return response.json();                             // ab yahan se jo bhi response return ho raha usko access karne ke liye ek aur then lagana padega
            })
            .then((data) => {                                        // upar ke json ko (data) me store karaya ja raha , taaki usko access kar sake
                //data. . . .
                //console.log(data);

                    let text = `<div class='list-group'>`           //ek div liya gaya hai aur bootstrap ki class lagayi gayi hai

                        data.forEach((contact) => {                         //multiple data recieve hone ke caseme loop lagya gaya hai
                            text+=`<a href='/user/${contact.cId}/contact' class='list-group-item list-group-item-action'> ${contact.name} </a>`  //result jo show hoga wo redirect kar dega uske page par
                        });

                        text+=`</div>`;

                $(".search-result").html(text);   // text html me change ho gaya. text variable me jo html likha gaya hai usko search-result me dynamically fetch kar diya gaya

                $(".search-result").show();  //result display isise hoga. jab koi valid string milegi, blank nahi rahega, to show kar dega result
            });


       

        /*fetch Api explain : jab fetch me url call hoga tab then me ye ho raha hai ke response jo aayga wo (response) function me aa jayega
            aur fir us response ko json me convert karke return kar diya.
            ab jo data return hokar aaya usko dusre then me (data) me store kara liya gaya hai.
            aur jo data aa chuka hai, usko simply String me apne according parse karke result me show kara dena hai
            search-result me show karane ke liye humko html mein inject karna hoga aur saare data ke liye loop bhi lagana hoga
            html() banane ke baad show() kara denge
        */ 
    }

}

