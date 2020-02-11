package com.google.firebase.ml.md.kotlin.Models

import com.google.firebase.ml.md.R

class BeverageCanList {
    companion object {
        var beverageCanList: ArrayList<BeverageCan>?=null
        var coke = BeverageCan(R.drawable.coke_no_sugar,"Coke", "325 ml.", "0",
                "0", "0",
                "0", "0",
                "25", "สีธรรมชาติ (INS150d) สารควบคุมการเป็นกรด (INS338,INS 331(iii)) สารให้ความหวาน (แอซีซัลเฟมโพแทสเซียมและซูคราโลส) แต่งกลิ่นธรรมชาติ วัตถุกันเสีย (INS 211) ไม่ใช่อาหารสำหรับควบคุมน้ำหนัก",
                "15")
        var pepsi = BeverageCan(R.drawable.pepsi_max_taste,"Pepsi", "245 ml.", "0",
                "0", "0",
                "0", "0",
                "0", "แต่งกลิ่นธรรมชาติ (สารควบคุมความเป็นกรด (INS330,INS331(iii),INS338) สีสังเคราะห์ (INS 150d) วัตถุกันเสีย (INS211) วัตถุให้ความหวานแทนน้ำตาล(แอสปาแตม,อะซีซัลเฟม โพแทสเซียม)",
                "12")
        var calpis = BeverageCan(R.drawable.calpis_lacto,"Calpis", "245 ml.", "60",
                "0", "< 1g",
                "14", "12",
                "40",
                "น้ำตาล 4.9% นมผงขาดมันเนย 0.73% นมเปรี้ยว 0.2% (หมักด้วยจุลินทรีย์ แลคโตบาซิลลัส) มีผลิตภัณฑ์จากนมและถั่วเหลือง สารควบคุมการเป็นกรด (INS 330,INS 331(iii))" +
                        " สารให้ความหวาน ซูคราโลสและแอซีซัลเฟม เค สารกันเสีย (INS 211) สารป้องกันการเกิดฟอง (INS 900a) สารให้ก๊าซคาร์บอนไดออกไซด์ (INS 290) แต่งกลิ่นเลียนแบบธรรมชาติ",
                "15")
        init {
            beverageCanList = ArrayList()
            beverageCanList?.add(coke)
            beverageCanList?.add(pepsi)
            beverageCanList?.add(calpis)
        }
    }
}