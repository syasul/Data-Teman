package com.example.datateman

class data_teman {
    var nama: String? = null
    var alamat: String? = null
    var no_hp: String? = null
    var prodi: String? = null
    var key: String? = null

    constructor(){}

    constructor(nama: String?, alamat: String?, no_hp: String?, prodi: String?){
        this.nama = nama
        this.alamat = alamat
        this.no_hp = no_hp
        this.prodi = prodi
    }
}
