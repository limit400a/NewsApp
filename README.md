# NewsApp - 新闻资讯客户端

一个基于 Kotlin + MVVM 架构的 Android 新闻资讯应用，展示现代 Android 开发技术栈的实践。

## 技术栈

| 层级 | 技术 |
|------|------|
| 编程语言 | Kotlin |
| UI 框架 | XML + RecyclerView + SwipeRefreshLayout |
| 架构模式 | MVVM + Repository |
| 网络请求 | Retrofit2 + Kotlin Coroutines |
| 图片加载 | Coil |
| 本地存储 | Room |
| 依赖管理 | Gradle |

## 功能特性

- [x] 新闻列表展示（RecyclerView + 图片懒加载）
- [x] 下拉刷新获取最新数据（SwipeRefreshLayout）
- [x] 新闻详情浏览（WebView）
- [x] 本地收藏功能（Room 数据库）
- [x] 多状态 UI 处理（加载中 / 空数据 / 错误 / 成功）

## 架构设计
UI 层 (Activity)  
↓ 观察 LiveData  
ViewModel 层 (NewsViewModel)  
↓ 调用  
Repository 层 (NewsRepository)  
↓
数据源（Retrofit / Room）

- **ViewModel**：持有 LiveData，处理业务逻辑，生命周期感知
- **Repository**：统一数据来源，封装网络请求和本地缓存
- **Retrofit**：RESTful API 网络请求
- **Room**：本地数据库，实现新闻收藏功能

## 项目亮点

### 1. MVVM 架构实践
- Activity 只负责 UI 展示，业务逻辑下沉到 ViewModel
- 屏幕旋转时数据不丢失（ViewModel 生命周期管理）

### 2. Kotlin 协程异步处理
- 使用 `viewModelScope.launch` 替代回调
- 配合 `suspend` 函数实现结构化并发

### 3. 多状态 UI 管理
- 加载状态：ProgressBar
- 成功状态：RecyclerView 展示列表
- 空数据状态：提示文字
- 错误状态：错误信息 + 重试机制

### 4. 图片加载优化
- Coil 异步加载，自动缓存
- 列表滑动时图片复用，避免 OOM

## 运行截图
<img width="381" height="814" alt="屏幕截图 2026-06-22 154523" src="https://github.com/user-attachments/assets/32f8be5d-e1d5-4c24-97ae-1f9843d406ef" />
<img width="373" height="811" alt="屏幕截图 2026-06-22 154541" src="https://github.com/user-attachments/assets/2b3e768f-40a5-4eda-b73c-ff0b01dbad9b" />
<img width="394" height="826" alt="屏幕截图 2026-06-22 155052" src="https://github.com/user-attachments/assets/2aeba14b-251e-42d5-ac28-6ca1228ae9ce" />

## 快速开始

### 环境要求
- Android Studio Hedgehog (2023.1.1) 或更高
- JDK 17
- Android SDK 34

### 运行步骤
1. 克隆项目
   ```bash
   git clone https://github.com/limit400a/NewsApp.git
2. 用 Android Studio 打开
3. 同步 Gradle
4. 运行到模拟器或真机  
### 配置说明  
使用 (https://www.juhe.cn/)[聚合数据] 新闻头条 API    
默认已配置 API Key，如需更换请修改 MainActivity 中的 API_KEY  
### 项目结构  
com.example.newsapp/  
├── data/  
│   ├── local/          # Room 数据库（Entity / Dao / Database）  
│   ├── remote/         # Retrofit 网络层（ApiService / RetrofitClient）  
│   └── News.kt         # 数据模型  
├── repository/  
│   └── NewsRepository.kt   # 仓库层，统一数据管理  
├── ui/  
│   ├── detail/         # 详情页（WebView）  
│   ├── news/           # 新闻列表（Adapter）  
│   └── MainActivity.kt  
└── MainActivity.kt  
### 关于
个人技术沉淀项目，用于展示现代 Android 开发能力。
