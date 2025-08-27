<template>
    <div class="login-container">
        <div class="cards-wrapper">
            <!-- 左侧区域 -->
            <div class="left-section">
                <div class="left-content">
                    <div class="title-logo-row">
                        <h2 class="want-to-text">Want to</h2>
                        <div class="logo-container">
                            <img src="../../assets/Slice1.png" alt="Logo" class="logo-image" />
                        </div>
                    </div>
                    <h2 class="sign-in-text">Sign in?</h2>
                    <button class="left-login-btn" @click="showLoginForm = true">
                        Login <span class="arrow">→</span>
                    </button>
                </div>
            </div>

            <!-- 中间连接卡片 -->
            <div class="connection-card"></div>

            <!-- 圆环装饰 -->
            <div class="circle-decoration"></div>

            <!-- 右侧区域 -->
            <div class="right-section">
            <div class="right-content">
                <div class="title-container">
                    <h1 class="welcome-title">Say Hello!</h1>
                    <h2 class="login-title">Login</h2>
                </div>

                <el-form ref="form" :model="userForm" class="login-form">
                    <div class="form-group">
                        <label class="form-label">Nickname</label>
                        <el-input

                            v-model="userForm.accountNumber"
                            class="form-input2"
                            placeholder="">
                        </el-input>
                    </div>

                    <div class="form-group">
                        <label class="form-label">Password</label>
                        <el-input
                            v-model="userForm.userPassword"
                            type="password"
                            class="form-input"
                            placeholder=""
                            @keyup.enter.native="login"
                            show-password>
                        </el-input>
                    </div>

                    <div class="button-group">
                        <el-button class="login-btn" @click="login">Login</el-button>
                        <el-button class="signup-btn" @click="$router.push('/sign-in')">Sign up</el-button>
                        <el-button class="admin-btn" @click="$router.push('/login-admin')">Admin</el-button>
                    </div>
                </el-form>
            </div>
        </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "login",
        data() {
            return {
                userForm: {
                    accountNumber: '',
                    userPassword: ''
                },
                showLoginForm: false
            };
        },

        methods: {
            login() {
                this.$api.userLogin({
                    accountNumber: this.userForm.accountNumber,
                    userPassword: this.userForm.userPassword
                }).then(res => {
                    console.log(res);
                    if (res.status_code === 1) {
                        res.data.signInTime=res.data.signInTime.substring(0,10);
                        this.$globalData.userInfo = res.data;
                        this.$router.replace({path: '/index'});
                    } else {
                        this.$message.error(res.msg);
                    }
                });
            },
            toIndex() {
                this.$router.replace({path: '/index'});
            }
        }
    }
</script>

<style scoped>
    @import url('https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&display=swap');

    .login-container {
        display: flex;
        height: 100vh;
        width: 100%;
        font-family: 'Abhaya Libre', serif;
        background: white;
        align-items: center;
        justify-content: center;
        padding: 20px;
        gap: 0;
        position: relative;
    }

    .cards-wrapper {
        display: flex;
        position: relative;
        max-width: 1000px;
        width: 100%;
        height: 600px;
        align-items: center;
        justify-content: center;
        margin: 0 auto;
    }

    /* 中间连接卡片样式 */
    .connection-card {
        width: 100px;
        height: 650px;
        background: #E1E6E7;
        border-radius: 40px;
        position: absolute;
        left: 45%;
        top: 50%;
        transform: translate(-50%, -50%);
        z-index: 2;
        box-shadow: 0 15px 40px rgba(225, 230, 231, 0.3), 0 8px 22px rgba(225, 230, 231, 0.2), 0 4px 12px rgba(225, 230, 231, 0.1);
    }

    /* 左侧区域样式 */
    .left-section {
        width: 500px;
        background: white;
        display: flex;
        align-items: center;
        justify-content: center;
        position: absolute;
        left: 15%;
        top: 50%;
        transform: translate(-50%, -50%);
        border-radius: 40px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1), 0 5px 15px rgba(0, 0, 0, 0.08), 0 2px 8px rgba(0, 0, 0, 0.05);
        padding: 80px 50px;
        height: 550px;
        z-index: 200;
    }

    .left-content {
        text-align: left;
        padding: 40px;
        width: 100%;
        height: 100%;
        position: relative;
        display: flex;
        flex-direction: column;
        justify-content: center;
    }

    .title-logo-row {
        display: flex;
        align-items: center;
        gap: 20px;
        margin-bottom: 20px;
    }

    .want-to-text {
        font-size: 56px;
        font-weight: 700;
        color: #16164F;
        margin: 0 0 -80px 00px;

        line-height: 1.1;
        font-family: 'Abhaya Libre', serif;
    }

    .logo-container {
        margin: 0;
        opacity: 1;
    }

    .logo-image {
        width: 160px;
        height: 160px;
        object-fit: contain;
    }

    .sign-in-text {
        font-size: 56px;
        font-weight: 700;
        color: #16164F;
        margin: -20px 20px 100px 200px;
        line-height: 1.1;
        font-family: 'Abhaya Libre', serif;
    }

    .left-login-btn {
        background: #0080A7;
        color: white;
        border: none;
        margin-top: 30px;
        padding:0 2px 0px 40px;
        border-radius: 50px;
        font-size: 48px;
        font-weight: 700;
        cursor: pointer;
        transition: all 0.3s ease;
        display: inline-flex;
        align-items: center;
        gap: 10px;
        font-family: 'Abhaya Libre', serif;
        box-shadow: 0 5px 15px rgba(0, 128, 167, 0.3);
        margin-left: 60px;
        align-self: flex-start;
    }

    .left-login-btn:hover {
        background: #006b8a;
        transform: translateY(-2px);
        box-shadow: 0 8px 20px rgba(0, 128, 167, 0.4);
    }

    .arrow {
        width: 100px;
        padding-right: 20px;
        margin: 0;
        font-size: 50px;
        transition: transform 0.3s ease;
    }

    .left-login-btn:hover .arrow {
        transform: translateX(5px);
    }

    /* 右侧区域样式 */
    .right-section {
        width: 700px;
        background: #16164F;
        display: flex;
        align-items: center;
        justify-content: center;
        position: absolute;
        right: 20%;
        top: 50%;
        transform: translate(50%, -50%);
        border-radius: 20px;
        height: 650px;
        z-index: 1;
        overflow: hidden;
    }

    .circle-decoration {
        position: absolute;
        width: 300px;
        height: 300px;
        border: 20px solid #0080A7;
        border-radius: 50%;
        background: transparent;
        top: 50%;
        right: 430px;
        transform: translateY(-50%);
        z-index: 100;
        pointer-events: none;
    }



    .right-content {
        background: transparent;
        padding: 60px 50px;
        width: 400px;
        position: relative;
        z-index: 3;
    }

    .welcome-title {
        font-size: 80px;
        font-weight: 700;
      margin-left: 400px;
        color: white;
        margin: 0;


        font-family: 'Abhaya Libre', serif;
    }

    .login-title {
        font-size: 56px;
        font-weight: 700;
        color: white;
        margin: 0;
        margin-left: 100px;
        font-family: 'Abhaya Libre', serif;
    }

    .title-container {
        text-align: center;
        margin-top: -20px;
        margin-bottom: 30px;
        margin-left: 40px;
        white-space: nowrap;
    }

    .login-form {
        width: 100%;
    }

    .form-group {
        margin-bottom: 25px;
    }

    .form-label {
        display: block;
        font-size: 32px;
        font-weight: 700;
        color: white;
        margin-bottom: 8px;
        font-family: 'Abhaya Libre', serif;
    }

    .form-input {
        width: 100%;
    }
        .form-input2 {
        width: 50%;
    }


    .form-input2 >>> .el-input__inner {
        border: 2px solid #e9ecef;
        border-radius: 25px;
        padding: 8px 20px;
        font-size: 16px;
        transition: all 0.3s ease;
        background: white;
        font-family: 'Abhaya Libre', serif;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .form-input2 >>> .el-input__inner:focus {
        border-color: #0080A7;
        background: white;
        box-shadow: 0 4px 12px rgba(0, 128, 167, 0.2), 0 0 0 3px rgba(0, 128, 167, 0.1);
    }

    .form-input >>> .el-input__inner {
        border: 2px solid #e9ecef;
        border-radius: 25px;
        padding: 8px 20px;
        font-size: 16px;
        transition: all 0.3s ease;
        background: white;
        font-family: 'Abhaya Libre', serif;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    }

    .form-input >>> .el-input__inner:focus {
        border-color: #0080A7;
        background: white;
        box-shadow: 0 4px 12px rgba(0, 128, 167, 0.2), 0 0 0 3px rgba(0, 128, 167, 0.1);
    }

    .button-group {
        display: flex;
        gap: 15px;
        margin-top: 30px;
        flex-wrap: nowrap;
        justify-content: space-between;
    }

    .login-btn {
        background: #0080A7;
        border: none;
        font-size: 36px;
        color: white;
        text-align: center;
        padding: 10px 50px;
        border-radius: 25px;
        font-weight: 700;
        flex: 1;
        min-width: 150px;
        font-family: 'Abhaya Libre', serif;
        box-shadow: 0 4px 12px rgba(0, 128, 167, 0.3);
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .login-btn:hover {
        background: #006b8a;
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(0, 128, 167, 0.4);
    }

    .signup-btn {
        background: #0080A7;
        border: none;
        font-size: 36px;
        color: white;
        text-align: center;
        padding: 10px 50px;
        border-radius: 25px;
        font-weight: 700;
        flex: 1;
        min-width: 150px;
        font-family: 'Abhaya Libre', serif;
        box-shadow: 0 4px 12px rgba(0, 128, 167, 0.3);
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .signup-btn:hover {
        background: #006b8a;
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(0, 128, 167, 0.4);
    }

    .admin-btn {
        background: #0080A7;
        border: none;
        font-size: 36px;
        color: white;
        text-align: center;
        padding: 10px 50px;
        border-radius: 25px;
        font-weight: 700;
        flex: 1;
        min-width: 150px;
        font-family: 'Abhaya Libre', serif;
        box-shadow: 0 4px 12px rgba(0, 128, 167, 0.3);
        transition: all 0.3s ease;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .admin-btn:hover {
        background: #006b8a;
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(0, 128, 167, 0.4);
    }

    /* 响应式设计 */
    @media (max-width: 768px) {
        .login-container {
            flex-direction: column;
        }

        .left-section,
        .right-section {
            flex: none;
            height: 50vh;
        }

        .left-title {
            font-size: 32px;
            font-family: 'Abhaya Libre', serif;
            font-weight: 700;
            color: #16164F;
        }

        .welcome-title {
            font-size: 28px;
            font-family: 'Abhaya Libre', serif;
            font-weight: 700;
            color: white;
        }

        .right-content {
            width: 90%;
            padding: 30px;
        }

        .button-group {
            flex-direction: column;
        }

        .btn {
            margin-bottom: 10px;
            font-family: 'Abhaya Libre', serif;
            font-weight: 700;
        }
    }
</style>
