function Sm4() {
    this.createContext = function () {
        this.mode = 1;
        this.sk = new Array(32);
        this.isPadding = true;
        return this;
    }

//byte转long
    this.GET_ULONG_BE = function (b, i) {
        var n = (((b[i] & 0xff) << 24) | ((b[i + 1] & 0xff) << 16) | ((b[i + 2] & 0xff) << 8) | (b[i + 3] & 0xff));
        return n;
    };

//long转byte
    this.PUT_ULONG_BE = function (n, b, i) {
        b[i] = (0xff & (n >> 24));
        b[i + 1] = (0xff & (n >> 16));
        b[i + 2] = (0xff & (n >> 8));
        b[i + 3] = (0xff & n);
    }

//左移n
    this.SHL = function (x, n) {
        return x << n;
    }

//循环左移n
    this.ROTL = function (x, n) {
        return this.SHL(x, n) | (x >> (32 - n));
    }

//交换sk的i位和31-i位
    this.SWAP = function (sk, i) {
        t = sk[i];
        sk[i] = sk[(31 - i)];
        sk[(31 - i)] = t;
    }

    var sboxTable = [
        0xd6, 0x90, 0xe9, 0xfe, 0xcc, 0xe1, 0x3d, 0xb7, 0x16, 0xb6, 0x14, 0xc2, 0x28, 0xfb, 0x2c, 0x05,
        0x2b, 0x67, 0x9a, 0x76, 0x2a, 0xbe, 0x04, 0xc3, 0xaa, 0x44, 0x13, 0x26, 0x49, 0x86, 0x06, 0x99,
        0x9c, 0x42, 0x50, 0xf4, 0x91, 0xef, 0x98, 0x7a, 0x33, 0x54, 0x0b, 0x43, 0xed, 0xcf, 0xac, 0x62,
        0xe4, 0xb3, 0x1c, 0xa9, 0xc9, 0x08, 0xe8, 0x95, 0x80, 0xdf, 0x94, 0xfa, 0x75, 0x8f, 0x3f, 0xa6,
        0x47, 0x07, 0xa7, 0xfc, 0xf3, 0x73, 0x17, 0xba, 0x83, 0x59, 0x3c, 0x19, 0xe6, 0x85, 0x4f, 0xa8,
        0x68, 0x6b, 0x81, 0xb2, 0x71, 0x64, 0xda, 0x8b, 0xf8, 0xeb, 0x0f, 0x4b, 0x70, 0x56, 0x9d, 0x35,
        0x1e, 0x24, 0x0e, 0x5e, 0x63, 0x58, 0xd1, 0xa2, 0x25, 0x22, 0x7c, 0x3b, 0x01, 0x21, 0x78, 0x87,
        0xd4, 0x00, 0x46, 0x57, 0x9f, 0xd3, 0x27, 0x52, 0x4c, 0x36, 0x02, 0xe7, 0xa0, 0xc4, 0xc8, 0x9e,
        0xea, 0xbf, 0x8a, 0xd2, 0x40, 0xc7, 0x38, 0xb5, 0xa3, 0xf7, 0xf2, 0xce, 0xf9, 0x61, 0x15, 0xa1,
        0xe0, 0xae, 0x5d, 0xa4, 0x9b, 0x34, 0x1a, 0x55, 0xad, 0x93, 0x32, 0x30, 0xf5, 0x8c, 0xb1, 0xe3,
        0x1d, 0xf6, 0xe2, 0x2e, 0x82, 0x66, 0xca, 0x60, 0xc0, 0x29, 0x23, 0xab, 0x0d, 0x53, 0x4e, 0x6f,
        0xd5, 0xdb, 0x37, 0x45, 0xde, 0xfd, 0x8e, 0x2f, 0x03, 0xff, 0x6a, 0x72, 0x6d, 0x6c, 0x5b, 0x51,
        0x8d, 0x1b, 0xaf, 0x92, 0xbb, 0xdd, 0xbc, 0x7f, 0x11, 0xd9, 0x5c, 0x41, 0x1f, 0x10, 0x5a, 0xd8,
        0x0a, 0xc1, 0x31, 0x88, 0xa5, 0xcd, 0x7b, 0xbd, 0x2d, 0x74, 0xd0, 0x12, 0xb8, 0xe5, 0xb4, 0xb0,
        0x89, 0x69, 0x97, 0x4a, 0x0c, 0x96, 0x77, 0x7e, 0x65, 0xb9, 0xf1, 0x09, 0xc5, 0x6e, 0xc6, 0x84,
        0x18, 0xf0, 0x7d, 0xec, 0x3a, 0xdc, 0x4d, 0x20, 0x79, 0xee, 0x5f, 0x3e, 0xd7, 0xcb, 0x39, 0x48];

    var CK = [
        0x00070e15, 0x1c232a31, 0x383f464d, 0x545b6269,
        0x70777e85, 0x8c939aa1, 0xa8afb6bd, 0xc4cbd2d9,
        0xe0e7eef5, 0xfc030a11, 0x181f262d, 0x343b4249,
        0x50575e65, 0x6c737a81, 0x888f969d, 0xa4abb2b9,
        0xc0c7ced5, 0xdce3eaf1, 0xf8ff060d, 0x141b2229,
        0x30373e45, 0x4c535a61, 0x686f767d, 0x848b9299,
        0xa0a7aeb5, 0xbcc3cad1, 0xd8dfe6ed, 0xf4fb0209,
        0x10171e25, 0x2c333a41, 0x484f565d, 0x646b7279];

    var FK = [0xa3b1bac6, 0x56aa3350, 0x677d9197, 0xb27022dc];

//8比特的s盒变换
    this.sm4Sbox = function (inch) {
        var i = inch & 0xff;
        var retVal = sboxTable[i];
        return retVal;
    }

//算法Lt(.)
    this.sm4Lt = function (ka) {
        var bb = 0;
        var c = 0;
        var a = new Array(4);
        var b = new Array(4);
        this.PUT_ULONG_BE(ka, a, 0);
        b[0] = this.sm4Sbox(a[0]);
        b[1] = this.sm4Sbox(a[1]);
        b[2] = this.sm4Sbox(a[2]);
        b[3] = this.sm4Sbox(a[3]);
        bb = this.GET_ULONG_BE(b, 0);
        c = bb ^ this.ROTL(bb, 2) ^ this.ROTL(bb, 10) ^ this.ROTL(bb, 18) ^ this.ROTL(bb, 24);
        return c;
    }

//算法F()，即一轮变换
    this.sm4F = function (x0, x1, x2, x3, rk) {
        return x0 ^ this.sm4Lt(x1 ^ x2 ^ x3 ^ rk);
    }


    this.sm4CalciRK = function (ka) {
        var bb = 0;
        var rk = 0;
        var a = new Array(4);
        var b = new Array(4);
        this.PUT_ULONG_BE(ka, a, 0);
        b[0] = this.sm4Sbox(a[0]);
        b[1] = this.sm4Sbox(a[1]);
        b[2] = this.sm4Sbox(a[2]);
        b[3] = this.sm4Sbox(a[3]);
        bb = this.GET_ULONG_BE(b, 0);
        rk = bb ^ this.ROTL(bb, 13) ^ this.ROTL(bb, 23);
        return rk;
    }

//轮密钥生成
    this.sm4_setkey = function (SK, key) {
        var MK = new Array(4);
        var k = new Array(36);
        var i = 0;
        // alert("aaa")
        MK[0] = this.GET_ULONG_BE(key, 0);
        MK[1] = this.GET_ULONG_BE(key, 4);
        MK[2] = this.GET_ULONG_BE(key, 8);
        MK[3] = this.GET_ULONG_BE(key, 12);
        k[0] = MK[0] ^ FK[0];
        k[1] = MK[1] ^ FK[1];
        k[2] = MK[2] ^ FK[2];
        k[3] = MK[3] ^ FK[3];
        for (; i < 32; i++) {
            k[(i + 4)] = (k[i] ^ this.sm4CalciRK(k[(i + 1)] ^ k[(i + 2)] ^ k[(i + 3)] ^ CK[i]));
            SK[i] = k[(i + 4)];
        }
        // alert("SK:"+SK.length+" "+SK[0])
    }

//
    this.sm4_rounds = function (sk, input, output) {
        i = 0;
        ulbuf = new Array(36);
        ulbuf[0] = this.GET_ULONG_BE(input, 0);
        ulbuf[1] = this.GET_ULONG_BE(input, 4);
        ulbuf[2] = this.GET_ULONG_BE(input, 8);
        ulbuf[3] = this.GET_ULONG_BE(input, 12);
        while (i < 32) {
            ulbuf[(i + 4)] = this.sm4F(ulbuf[i], ulbuf[(i + 1)], ulbuf[(i + 2)], ulbuf[(i + 3)], sk[i]);
            i++;
        }
        this.PUT_ULONG_BE(ulbuf[35], output, 0);
        this.PUT_ULONG_BE(ulbuf[34], output, 4);
        this.PUT_ULONG_BE(ulbuf[33], output, 8);
        this.PUT_ULONG_BE(ulbuf[32], output, 12);
    }

    this.padding = function (input, mode) {
        if (input == undefined) {
            return null;
        }

        var ret = [];

        ret = ret.concat(input);
        if (mode == 1) {
            var p = 16 - input.length % 16;
            for (i = 0; i < p; i++) {
                ret[input.length + i] = p;
            }
        } else {
            var p = input[input.length - 1];
            for (var t = 0; t < p; t++) {
                ret.pop();
            }
        }
        return ret;
    }

//生成加密密钥
    this.sm4_setkey_enc = function (ctx, key) {

        if (ctx == undefined) {
            Error("ctx is null!");
        }

        if (key == undefined || key.length != 16) {
            Error("key error!");
        }
        // alert("ctx"+ctx.sk.length);
        ctx.mode = 1;
        this.sm4_setkey(ctx.sk, key);
    }

//生成解密密钥
    this.sm4_setkey_dec = function (ctx, key) {
        if (ctx == null) {
            Error("ctx is null!");
        }

        if (key == null || key.length != 16) {
            Error("key error!");
        }

        var i = 0;
        ctx.mode = 0;
        this.sm4_setkey(ctx.sk, key);
        ctx.sk = ctx.sk.reverse();
    }

    this.sm4_crypt_ecb = function (ctx, input) {
        // alert("input"+input[0]+input[1]);
        // alert("input-size"+input.length);
        if (input == null | input == '') {
            Error("input is null!");
        }
        if ((ctx.isPadding) && (ctx.mode == 1)) {
            input = this.padding(input, 1);
        }
        // alert("input-size"+input.length)
        var length = input.length;
        var bous = [];
        for (var t = 0; t < length; t += 16) {
            var inn = [];
            var out = [];
            inn.push(input[t + 0], input[t + 1], input[t + 2], input[t + 3], input[t + 4],
                input[t + 5], input[t + 6], input[t + 7], input[t + 8], input[t + 9], input[t + 10],
                input[t + 11], input[t + 12], input[t + 13], input[t + 14], input[t + 15]);
            // alert("inn-size"+inn.length);
            this.sm4_rounds(ctx.sk, inn, out);
            // alert("out-size"+out.length)
            bous = bous.concat(out);
        }

        // alert("bous-size"+bous.length);
        if (ctx.isPadding && ctx.mode == 0) {
            bous = this.padding(bous, 0);
        }
        return bous;
    }

    this.sm4_crypt_cbc = function (ctx, iv, input) {
        if (iv == null || iv.length != 16) {
            alert("iv error!");
        }

        if (input == null) {
            alert("input is null!");
        }

        if (ctx.isPadding && ctx.mode == 1) {
            input = this.padding(input, 1);
        }

        var i = 0;
        var length = input.length;
        var bous = new Array();
        if (ctx.mode == 1) {
            var k = 0;
            for (; length > 0; length -= 16) {
                var out = new Array(16);
                var out1 = new Array(16);
                var ins = input.slice(k * 16, (16 * (k + 1)));

                for (i = 0; i < 16; i++) {
                    out[i] = (ins[i] ^ iv[i]);
                }
                this.sm4_one_round(ctx.sk, out, out1);
                iv = out1.slice(0, 16);
                bous = bous.concat(out1);
                k++;
            }
        } else {
            var temp = [];
            var k = 0;
            for (; length > 0; length -= 16) {
                var out = new Array(16);
                var out1 = new Array(16);
                var ins = input.slice(k * 16, (16 * (k + 1)));
                temp = ins.slice(0, 16);
                this.sm4_one_round(ctx.sk, ins, out);
                for (i = 0; i < 16; i++) {
                    out1[i] = (out[i] ^ iv[i]);
                }
                iv = temp.slice(0, 16);
                bous = bous.concat(out1);
                k++;
            }
        }

        var output = bous;
        if (ctx.isPadding && ctx.mode == 0) {
            output = this.padding(output, 0);
        }

        for (var i = 0; i < output.length; i++) {
            if (output[i] < 0) {
                output[i] = output[i] + 256;
            }
        }
        return output;
    }

    this.sm4_one_round = function (sk, input, output) {
        var i = 0;
        var ulbuf = new Array(36);
        ulbuf[0] = this.GET_ULONG_BE(input, 0);
        ulbuf[1] = this.GET_ULONG_BE(input, 4);
        ulbuf[2] = this.GET_ULONG_BE(input, 8);
        ulbuf[3] = this.GET_ULONG_BE(input, 12);
        while (i < 32) {
            ulbuf[(i + 4)] = this.sm4F(ulbuf[i], ulbuf[(i + 1)], ulbuf[(i + 2)], ulbuf[(i + 3)], sk[i]);
            i++;
        }
        this.PUT_ULONG_BE(ulbuf[35], output, 0);
        this.PUT_ULONG_BE(ulbuf[34], output, 4);
        this.PUT_ULONG_BE(ulbuf[33], output, 8);
        this.PUT_ULONG_BE(ulbuf[32], output, 12);
    }

    this.isNull = function (obj) {
        for (var a in obj) {
            return false;
        }
        return ture;
    }

    this.stringToByte = function (str) {
        var bytes = new Array();
        var len, c;
        len = str.length;
        for (var i = 0; i < len; i++) {
            c = str.charCodeAt(i);
            if (c >= 0x010000 && c <= 0x10FFFF) {
                bytes.push(((c >> 18) & 0x07) | 0xF0);
                bytes.push(((c >> 12) & 0x3F) | 0x80);
                bytes.push(((c >> 6) & 0x3F) | 0x80);
                bytes.push((c & 0x3F) | 0x80);
            } else if (c >= 0x000800 && c <= 0x00FFFF) {
                bytes.push(((c >> 12) & 0x0F) | 0xE0);
                bytes.push(((c >> 6) & 0x3F) | 0x80);
                bytes.push((c & 0x3F) | 0x80);
            } else if (c >= 0x000080 && c <= 0x0007FF) {
                bytes.push(((c >> 6) & 0x1F) | 0xC0);
                bytes.push((c & 0x3F) | 0x80);
            } else {
                bytes.push(c & 0xFF);
            }
        }
        return bytes;


    }


    this.byteToString = function (arr) {
        if (typeof arr === 'string') {
            return arr;
        }
        var str = '',
            _arr = arr;
        for (var i = 0; i < _arr.length; i++) {
            var one = _arr[i].toString(2),
                v = one.match(/^1+?(?=0)/);
            if (v && one.length == 8) {
                var bytesLength = v[0].length;
                var store = _arr[i].toString(2).slice(7 - bytesLength);
                for (var st = 1; st < bytesLength; st++) {
                    store += _arr[st + i].toString(2).slice(2);
                }
                str += String.fromCharCode(parseInt(store, 2));
                i += bytesLength - 1;
            } else {
                str += String.fromCharCode(_arr[i]);
            }
        }
        return str;
    }
}





//base64.js

function Base64js() {
    var r, e, t;
    return function r(e, t, n) {
        function o(i, a) {
            if (!t[i]) {
                if (!e[i]) {
                    var u = typeof require == "function" && require;
                    if (!a && u) {
                        return u(i, !0)
                    }
                    if (f) {
                        return f(i, !0)
                    }
                    var d = new Error("Cannot find module '" + i + "'");
                    throw d.code = "MODULE_NOT_FOUND", d
                }
                var c = t[i] = {exports: {}};
                e[i][0].call(c.exports, function (r) {
                    var t = e[i][1][r];
                    return o(t ? t : r)
                }, c, c.exports, r, e, t, n)
            }
            return t[i].exports
        }

        var f = typeof require == "function" && require;
        for (var i = 0; i < n.length; i++) {
            o(n[i])
        }
        return o
    }({
        "/": [function (r, e, t) {
            t.byteLength = c;
            t.toByteArray = v;
            t.fromByteArray = s;
            var n = [];
            var o = [];
            var f = typeof Uint8Array !== "undefined" ? Uint8Array : Array;
            var i = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
            for (var a = 0, u = i.length; a < u; ++a) {
                n[a] = i[a];
                o[i.charCodeAt(a)] = a
            }
            o["-".charCodeAt(0)] = 62;
            o["_".charCodeAt(0)] = 63;

            function d(r) {
                var e = r.length;
                if (e % 4 > 0) {
                    throw new Error("Invalid string. Length must be a multiple of 4")
                }
                return r[e - 2] === "=" ? 2 : r[e - 1] === "=" ? 1 : 0
            }

            function c(r) {
                return r.length * 3 / 4 - d(r)
            }

            function v(r) {
                var e, t, n, i, a;
                var u = r.length;
                i = d(r);
                a = new f(u * 3 / 4 - i);
                t = i > 0 ? u - 4 : u;
                var c = 0;
                for (e = 0; e < t; e += 4) {
                    n = o[r.charCodeAt(e)] << 18 | o[r.charCodeAt(e + 1)] << 12 | o[r.charCodeAt(e + 2)] << 6 | o[r.charCodeAt(e + 3)];
                    a[c++] = n >> 16 & 255;
                    a[c++] = n >> 8 & 255;
                    a[c++] = n & 255
                }
                if (i === 2) {
                    n = o[r.charCodeAt(e)] << 2 | o[r.charCodeAt(e + 1)] >> 4;
                    a[c++] = n & 255
                } else {
                    if (i === 1) {
                        n = o[r.charCodeAt(e)] << 10 | o[r.charCodeAt(e + 1)] << 4 | o[r.charCodeAt(e + 2)] >> 2;
                        a[c++] = n >> 8 & 255;
                        a[c++] = n & 255
                    }
                }
                return a
            }

            function l(r) {
                return n[r >> 18 & 63] + n[r >> 12 & 63] + n[r >> 6 & 63] + n[r & 63]
            }

            function h(r, e, t) {
                var n;
                var o = [];
                for (var f = e; f < t; f += 3) {
                    n = (r[f] << 16) + (r[f + 1] << 8) + r[f + 2];
                    o.push(l(n))
                }
                return o.join("")
            }

            function s(r) {
                var e;
                var t = r.length;
                var o = t % 3;
                var f = "";
                var i = [];
                var a = 16383;
                for (var u = 0, d = t - o; u < d; u += a) {
                    i.push(h(r, u, u + a > d ? d : u + a))
                }
                if (o === 1) {
                    e = r[t - 1];
                    f += n[e >> 2];
                    f += n[e << 4 & 63];
                    f += "=="
                } else {
                    if (o === 2) {
                        e = (r[t - 2] << 8) + r[t - 1];
                        f += n[e >> 10];
                        f += n[e >> 4 & 63];
                        f += n[e << 2 & 63];
                        f += "="
                    }
                }
                i.push(f);
                return i.join("")
            }
        }, {}]
    }, {}, [])("/")
};

//smutils.js
//封装sm4.js，实现ECB工作模式
function sm4utils(key) {
    this.seckey = key;
    this.encryptData_ECB = encryptData_ECB;
    this.decryptData_ECB = decryptData_ECB;
    var sm4 =new Sm4();
    var base64 = new Base64js();
    // this.hexString = false;
    function encryptData_ECB(plainText) {

        var ctx = sm4.createContext();

        ctx.isPadding = true;
        ctx.mode = 1;
        var keyBytes;
        try {
            if (this.seckey == null || this.seckey.length != 16) {
                throw "key 不规范,长度必须16"
            }
            keyBytes = sm4.stringToByte(this.seckey);

        } catch (e) {
            Error(e.message);
        }
        // alert("key"+keyBytes.length)
        sm4.sm4_setkey_enc(ctx, keyBytes);
        var encrypted = sm4.sm4_crypt_ecb(ctx, sm4.stringToByte(plainText));
        var cipherText = base64.fromByteArray(encrypted);
        if (cipherText != null && cipherText.trim().length > 0) {
            cipherText.replace(/(\s*|\t|\r|\n)/g, "");
        }
        // alert(cipherText);
        return cipherText;
    }

    function decryptData_ECB(cipherText) {
        try {
            var ctx = sm4.createContext();
            ctx.isPadding = true;
            ctx.mode = 0;

            var keyBytes = sm4.stringToByte(this.seckey);
            sm4.sm4_setkey_dec(ctx, keyBytes);
            var decrypted = sm4.sm4_crypt_ecb(ctx, base64.toByteArray(cipherText));
            return sm4.byteToString(decrypted);
        } catch (e) {
            Error(e.message);
            return null;
        }
    }

    this.encryptData_CBC = function (plainText) {
        try {
            var sm4 =new Sm4();
            var ctx = sm4.createContext();
            ctx.isPadding = true;
            ctx.mode = 1;

            var keyBytes = sm4.stringToByte(this.seckey);
            var ivBytes = sm4.stringToByte(this.iv);

            sm4.sm4_setkey_enc(ctx, keyBytes);
            var encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, sm4.stringToByte(plainText));
            var cipherText = base64.fromByteArray(encrypted);
            if (cipherText != null && cipherText.trim().length > 0) {
                cipherText.replace(/(\s*|\t|\r|\n)/g, "");
            }
            return cipherText;
        } catch (e) {
            console.error(e);
            return null;
        }
    }

    this.decryptData_CBC = function (cipherText) {
        try {
            var sm4 =new Sm4();
            var ctx = sm4.createContext();
            ctx.isPadding = true;
            ctx.mode = 0;

            var keyBytes = sm4.stringToByte(this.seckey);
            var ivBytes = sm4.stringToByte(this.iv);

            sm4.sm4_setkey_dec(ctx, keyBytes);
            var encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, base64.toByteArray(cipherText));
            return sm4.byteToString(encrypted);
        } catch (e) {
            console.error(e);
            return null;
        }
    }
}