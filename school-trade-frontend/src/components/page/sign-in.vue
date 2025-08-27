<template>
    <div class="sign-in-container">
        <div class="cards-wrapper">
            <!-- 左侧区域 -->
            <div class="left-section">
                <div class="left-content">
                    <div class="title-logo-row">
                        <h2 class="want-to-text">Want to</h2>
                        <div class="logo-container">
                            <img src="../../assets/Slice2.png" alt="Logo" class="logo-image" />
                        </div>
                    </div>
                    <h2 class="sign-up-text">Log in?</h2>
                    <button class="left-signup-btn" @click="showSignupForm = true">
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
                        <h1 class="welcome-title">Sign up</h1>
                    </div>

                    <el-form ref="form" :model="userInfo" class="signup-form">
                        <div class="form-group">
                            <label class="form-label">Nickname</label>
                            <el-input
                                v-model="userInfo.nickname"
                                class="form-input"
                                placeholder=""
                                maxlength="30"
                                clearable>
                            </el-input>
                        </div>

                        <div class="form-group">
                            <label class="form-label">UPI</label>
                            <el-input
                                v-model="userInfo.accountNumber"
                                class="form-input"
                                placeholder=""
                                maxlength="11"
                                clearable>
                            </el-input>
                        </div>

                        <div class="form-group">
                            <label class="form-label">verification code</label>
                            <div class="verification-input-group">
                                <el-input
                                    v-model="emailCode"
                                    class="form-input verification-input"
                                    placeholder=""
                                    clearable>
                                </el-input>
                                <el-button
                                    class="get-code-btn"
                                    @click="sendCode"
                                    :disabled="codeBtnDisabled">
                                    {{codeBtnText}}
                                </el-button>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Set password</label>
                            <el-input
                                v-model="userInfo.userPassword"
                                type="password"
                                class="form-input"
                                placeholder=""
                                maxlength="16"
                                show-password
                                clearable>
                            </el-input>
                        </div>

                        <div class="form-group">
                            <label class="form-label">Confirm password</label>
                            <el-input
                                v-model="userPassword2"
                                type="password"
                                class="form-input"
                                placeholder=""
                                maxlength="16"
                                show-password
                                clearable
                                @keyup.enter.native="signIn">
                            </el-input>
                        </div>

                        <div class="button-group">
                            <el-button class="submit-btn" @click="signIn">Submit</el-button>
                            <el-button class="back-btn" @click="toLogin">Back to login</el-button>
                        </div>
                    </el-form>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "sign-in",
        data(){
            return{
                userPassword2:'',
                emailCode:'',
                codeBtnText:'Get',
                codeBtnDisabled:false,
                countdown:60,
                timer:null,
                showSignupForm: false,
                userInfo:{
                    accountNumber:'',
                    userPassword:'',
                    nickname:''
                }
            };
        },
        methods:{
            toLogin(){
                this.$router.replace({path: '/login'});
            },
            sendCode(){
                if(!this.userInfo.accountNumber){
                    this.$message.error('Please enter UPI first!');
                    return;
                }
                this.$api.getEmailCode({upi:this.userInfo.accountNumber}).then(res=>{
                    if(res.status_code===1){
                        this.$message.success('Verification code sent');
                        this.codeBtnDisabled=true;
                        this.codeBtnText=this.countdown+'s';
                        this.timer=setInterval(()=>{
                            this.countdown--;
                            this.codeBtnText=this.countdown+'s';
                            if(this.countdown<=0){
                                clearInterval(this.timer);
                                this.countdown=60;
                                this.codeBtnText='Get';
                                this.codeBtnDisabled=false;
                            }
                        },1000);
                    }else{
                        this.$message.error(res.msg);
                    }
                }).catch(()=>{
                    this.$message.error('Failed to send verification code');
                });
            },
            signIn(){
                if(this.userInfo.accountNumber&&this.userInfo.userPassword&&this.userInfo.nickname&&this.emailCode){
                    if(this.userInfo.userPassword!==this.userPassword2){
                        this.$message.error('Passwords do not match!');
                    }else {
                        this.$api.signIn({...this.userInfo,code:this.emailCode}).then(res=>{
                            if(res.status_code===1){
                                this.$message({
                                    message: 'Registration successful!',
                                    type: 'success'
                                });
                                this.$router.replace({path: '/login'});
                            }else {
                                this.$message.error(res.msg);
                            }
                        }).catch(e=>{
                            console.log(e);
                            this.$message.error('Registration failed, network error!');
                        })
                    }
                }else{
                    this.$message.error('Please fill in all registration information!');
                }
            }
        }
    }
</script>

<style scoped>
    @import url('https://fonts.googleapis.com/css2?family=Abhaya+Libre:wght@400;500;600;700;800&display=swap');

    .sign-in-container {
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
        height: 750px;
        align-items: center;
        justify-content: center;
        margin: 0 auto;
    }

    /* 中间连接卡片样式 */
    .connection-card {
        width: 100px;
        height: 800px;
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
        background: #16164F;
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
        height: 700px;
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
        color: white;
        margin: 0 0 -80px 0;
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

    .sign-up-text {
        font-size: 56px;
        font-weight: 700;
        color: white;
        margin: -20px 20px 100px 200px;
        line-height: 1.1;
        font-family: 'Abhaya Libre', serif;
    }

    .left-signup-btn {
        background: #0080A7;
        color: white;
        border: none;
        margin-top: 30px;
        padding: 0 2px 0px 40px;
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

    .left-signup-btn:hover {
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

    .left-signup-btn:hover .arrow {
        transform: translateX(5px);
    }

    /* 右侧区域样式 */
    .right-section {
        width: 700px;
        background: #F1F6F7;
        display: flex;
        align-items: center;
        justify-content: center;
        position: absolute;
        right: 20%;
        top: 50%;
        transform: translate(50%, -50%);
        border-radius: 20px;
        height: 800px;
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
        color: #16164F;
        margin: 0;
        display: inline-block;
        font-family: 'Abhaya Libre', serif;
    }

    .signup-title {
        font-size: 80px;
        font-weight: 700;
        color: #16164F;
        margin: 0;
        margin-left: 20px;
        display: inline-block;
        font-family: 'Abhaya Libre', serif;
    }

    .title-container {
        text-align: center;
        margin-top: -20px;
        margin-bottom: 30px;
        margin-left: 40px;
        white-space: nowrap;
    }

    .signup-form {
        width: 100%;
    }

    .form-group {
        margin-bottom: 25px;
    }

    .form-label {
        display: block;
        font-size: 32px;
        font-weight: 700;
        color: #16164F;
        margin-bottom: 8px;
        font-family: 'Abhaya Libre', serif;
    }

    .form-input {
        width: 100%;
    }

    .form-input >>> .el-input__inner {
        border: 2px solid #e9ecef;
        border-radius: 25px;
        padding: 12px 20px;
        font-size: 16px;
        transition: all 0.3s ease;
        background: white;
        font-family: 'Abhaya Libre', serif;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        height: 50px;
    }

    .form-input >>> .el-input__inner:focus {
        border-color: #0080A7;
        background: white;
        box-shadow: 0 4px 12px rgba(0, 128, 167, 0.2), 0 0 0 3px rgba(0, 128, 167, 0.1);
    }

    .verification-input-group {
        display: flex;
        gap: 10px;
        align-items: center;
    }

    .verification-input {
        flex: 1;
    }

    .get-code-btn {
        background: #0080A7;
        border: none;
        color: white;
        padding: 12px 30px;
        border-radius: 25px;
        font-size: 32px;
        font-weight: 600;
        cursor: pointer;
        transition: all 0.3s ease;
        font-family: 'Abhaya Libre', serif;
        box-shadow: 0 2px 8px rgba(0, 128, 167, 0.3);
        min-width: 120px;
        height: 50px;
    }

    .get-code-btn:hover {
        background: #006b8a;
        transform: translateY(-1px);
        box-shadow: 0 4px 12px rgba(0, 128, 167, 0.4);
    }

    .get-code-btn:disabled {
        background: #ccc;
        cursor: not-allowed;
        transform: none;
        box-shadow: none;
    }

    .button-group {
        display: flex;
        gap: 15px;
        margin-top: 30px;
        flex-wrap: nowrap;
        justify-content: space-between;
    }

    .submit-btn {
        background: #0080A7;
        border: none;
        font-size: 32px;
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

    .submit-btn:hover {
        background: #006b8a;
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(0, 128, 167, 0.4);
    }

    .back-btn {
        background: #0080A7;
        border: none;
        font-size: 32px;
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

    .back-btn:hover {
        background: #006b8a;
        transform: translateY(-2px);
        box-shadow: 0 6px 16px rgba(0, 128, 167, 0.4);
    }

    /* 响应式设计 */
    @media (max-width: 768px) {
        .sign-in-container {
            flex-direction: column;
        }

        .left-section,
        .right-section {
            flex: none;
            height: 50vh;
        }

        .want-to-text {
            font-size: 64px;
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

        .submit-btn,
        .back-btn {
            margin-bottom: 10px;
            font-family: 'Abhaya Libre', serif;
            font-weight: 700;
        }
    }
</style>
