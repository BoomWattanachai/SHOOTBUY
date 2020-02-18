package com.google.firebase.ml.md.kotlin.Models

import com.google.firebase.ml.md.R

class FurnitureList {
    companion object {
        var furnitureList: ArrayList<Furniture>? = null
        var gamingChair = Furniture(R.drawable.gaming_chair, "Gaming_chair", "FANTECH", "GC-181A", "ดำ-แดง", "45*45*90", "โครงสร้างด้วยโครงเหล็กหนาแน่น\n" +
                "รองรับน้ำหนักได้สูงสุด 150 กก.\n" +
                "ตัวเบาะผลิตจากวัสดุหนังเทียม PU\n" +
                "ปรับองศาของเก้าอี้ได้ถึง 180 องศา", "3,890")
        var officeChair = Furniture(R.drawable.office_chair, "Office_chair", "เออร์โกเทรน", "Dual-03RFF", "ดำ-แดง", "ขนาดที่นั่ง (กว้าง x ลึก) : 50 × 50 ซม.\nขนาดสินค้า (กว้าง x ลึก x สูง) : 64 × 60 × 110-122 ซม. / ตัว", "เก้าอี้สำนักงานเพื่อสุขภาพ\n" +
                "ที่นั่งโครงพลาสติกบุฟองน้ำหุ้มหนัง PU leather\n" +
                "รองรับน้ำหนักได้สูงสุด 120 กก.\n" +
                "สินค้ารับประกัน 1 ปี ", "6,900")
        var wickerChair = Furniture(R.drawable.wicker_chair, "Wicker_chair", "IKEA", "AGEN อาเก้น", "น้ำตาล(หวาย)", "ความกว้าง: 58 ซม.\n" +
                "ความลึก: 56 ซม.\n" +
                "ความสูง: 79 ซม.\n" +
                "ความกว้างที่นั่ง: 43 ซม.\n" +
                "ความลึกที่นั่ง: 40 ซม.\n" +
                "ความสูงที่นั่ง: 44 ซม.",
                "เก้าอี้ซ้อนเก็บได้\n" +
                        "ขาทำจากพลาสติก\n" +
                        "คำแนะนำ\n" +
                        "ใช้คู่กับแผ่นรองนั่งรุ่น NORNA", "1,490")
        var woodChair = Furniture(R.drawable.wood_chair, "Wood_chair", "K'DAUZ", "KD-CHR-00001N", "สีธรรมชาติ", "94cm x 44cm", "เก้าอี้ไม่ประดู่\n\n\n", "1,600")
        var coolingFan = Furniture(R.drawable.cool_fan, "Cooling_fan", "HATARI", "AC TURBO 1", "-", "ขนาดใบพัด(นิ้ว):13", "ประเภท:พัดลมไอเย็นบรรจุน้ำ32ลิตร\nปรับแรงลม(ระดับ):5\nรับประกันมอเตอร์(ปี):3\nRemoteControl:มี", "4,888")
        var monitor = Furniture(R.drawable.monitor, "Monitor", "ACER", "K242HQLBbid", "ดำ", "23.6นิ้ว", "Maximum Resolution:1920x1080\nBrightness:250 cd/m2\nResponse Time:5ms\nHDMI:1 Port", "3,690")
        var notebook = Furniture(R.drawable.nootbook, "Notebook", "ACER", "NITRO AN515-52", "Shale Black", "Screen Size: 15.6 inch", "Processor:Intel Core i5-8300H\n" +
                "Memory:8 GB DDR4\n" +
                "Storage:512 GB SSD\n" +
                "Warranty:3 Years", "19,900")

        init {
            furnitureList = ArrayList()
            furnitureList?.add(gamingChair)
            furnitureList?.add(officeChair)
            furnitureList?.add(wickerChair)
            furnitureList?.add(woodChair)
            furnitureList?.add(coolingFan)
            furnitureList?.add(monitor)
            furnitureList?.add(notebook)

        }
    }
}