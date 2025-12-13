# 🚀 **Guía Definitiva: JAR Único con Frontend + Backend**

## 📁 **Estructura del Proyecto**
```
project-root/
├── pom.xml              # PADRE (com.github.maximovj:rhhub)
├── backend/
│   ├── pom.xml         # Spring Boot (JAR final)
│   └── src/
└── frontend/
    ├── pom.xml         # Vue 3 (construye dist/)
    ├── package.json
    └── vite.config.js
```

---

## ⚙️ **Configuración CORRECTA**

### **1. `project-root/pom.xml` (PADRE)**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.github.maximovj</groupId>
    <artifactId>rhhub</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>pom</packaging>
    
    <!-- ORDEN CRÍTICO: frontend PRIMERO -->
    <modules>
        <module>frontend</module>
        <module>backend</module>
    </modules>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.4.0</version>
        <relativePath/>
    </parent>
</project>
```

### **2. `frontend/pom.xml` (CONSTRUYE Vue)**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.github.maximovj</groupId>
        <artifactId>rhhub</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>
    
    <artifactId>frontend</artifactId>
    <packaging>pom</packaging>
    
    <build>
        <plugins>
            <plugin>
                <groupId>com.github.eirslett</groupId>
                <artifactId>frontend-maven-plugin</artifactId>
                <version>1.15.0</version>
                <configuration>
                    <skip>false</skip>           <!-- ¡EJECUTA npm! -->
                    <skipInstall>true</skipInstall> <!-- Ya tienes Node.js -->
                    <workingDirectory>./</workingDirectory>
                </configuration>
                <executions>
                    <execution>
                        <id>npm-install</id>
                        <goals><goal>npm</goal></goals>
                        <phase>generate-resources</phase>
                        <configuration>
                            <arguments>install</arguments>
                        </configuration>
                    </execution>
                    <execution>
                        <id>npm-build</id>       <!-- ¡ESTO construye dist/ -->
                        <goals><goal>npm</goal></goals>
                        <phase>prepare-package</phase>
                        <configuration>
                            <arguments>run build</arguments>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

### **3. `backend/pom.xml` (JAR FINAL)**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project>
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.github.maximovj</groupId>
        <artifactId>rhhub</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>
    
    <artifactId>backend</artifactId>
    <packaging>jar</packaging>
    
    <build>
        <plugins>
            <!-- COPIA frontend/dist a static/ -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-resources-plugin</artifactId>
                <version>3.3.1</version>
                <executions>
                    <execution>
                        <id>copy-frontend</id>
                        <!-- DESPUÉS de que frontend construya -->
                        <phase>prepare-package</phase>
                        <goals><goal>copy-resources</goal></goals>
                        <configuration>
                            <outputDirectory>${project.build.outputDirectory}/static</outputDirectory>
                            <resources>
                                <resource>
                                    <directory>../frontend/dist</directory>
                                </resource>
                            </resources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
            
            <!-- Spring Boot JAR -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 🛠️ **4️⃣ Cómo se Construye TODO**

📍 **Siempre desde `project-root`**
```bash
mvn clean install
```

**Orden real:**
1️⃣ **Frontend** → `npm run build` (crea `frontend/dist/`)  
2️⃣ **Backend** → copia `dist/` a `classes/static/`  
3️⃣ **Spring Boot** → empaqueta JAR único  

---

## 📦 **Resultado Final**

```
backend/target/backend-0.0.1-SNAPSHOT.jar
 └── BOOT-INF/classes/static/
     ├── index.html          # ← Vue SPA
     ├── favicon.ico
     └── assets/
         ├── app-xxx.js
         └── app-xxx.css
```

**Ejecutas:**
```bash
java -jar backend/target/backend-0.0.1-SNAPSHOT.jar
```

**Accedes:**
- 🌐 **Frontend:** `http://localhost:8080/`
- 🔧 **API Backend:** `http://localhost:8080/api/...`

---

## ⚡ **Comandos Rápidos**

```bash
# 📦 CONSTRUIR TODO
mvn clean install

# 🔍 VER qué hay en el JAR
jar tf backend/target/backend-0.0.1-SNAPSHOT.jar | findstr static

# 🚀 EJECUTAR
java -jar backend/target/backend-0.0.1-SNAPSHOT.jar

# 🧪 SOLO backend (construye frontend primero)
mvn clean install -pl backend -am

# 🎯 SOLO frontend (para pruebas)
cd frontend
npm run build
```

---

## ✅ **Verificación Paso a Paso**

```bash
# 1. Limpiar
mvn clean

# 2. Construir frontend (debería crear dist/)
mvn install -pl frontend
# Verifica: ls frontend/dist/

# 3. Construir backend (copia y empaqueta)
mvn install -pl backend

# 4. Ejecutar
java -jar backend/target/backend-0.0.1-SNAPSHOT.jar
```

---

## ⚠️ **Si No Funciona**

### **Problema 1:** `frontend/dist/` no se crea
```bash
# Construir Vue manualmente
cd frontend
npm install
npm run build
# Ver: ls dist/
```

### **Problema 2:** JAR no incluye `static/`
```xml
<!-- En backend/pom.xml, CAMBIA: -->
<phase>prepare-package</phase>  <!-- NO process-resources -->
```

### **Problema 3:** Errores de Vue
```javascript
// frontend/vite.config.js
export default {
  build: {
    outDir: 'dist',  // ← Asegura que salga aquí
    emptyOutDir: true
  }
}
```

---

## 🎯 **Resumen Visual**
```
[Vue Source] → npm run build → [dist/] → Maven copia → [JAR static/] → Java ejecuta
      ↑                                    ↑                    ↑
   frontend/                          backend/pom.xml    backend/target/*.jar
```

**¡Un solo comando, un solo JAR, aplicación completa!** 🚀