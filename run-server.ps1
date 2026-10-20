param(
    [string]$JdkHome = $env:JAVA_HOME,
    [ValidateRange(1024,65535)][int]$Port = 8091
)
$ErrorActionPreference = 'Stop'
if (-not $JdkHome -or -not (Test-Path -LiteralPath (Join-Path $JdkHome 'bin\java.exe'))) {
    throw 'JDK 21의 실제 폴더를 -JdkHome 매개변수로 지정하세요.'
}
$taskOldJava = $env:JAVA_HOME
$taskOldPath = $env:PATH
Push-Location $PSScriptRoot
try {
    $env:JAVA_HOME = $JdkHome
    $env:PATH = (Join-Path $JdkHome 'bin') + ';' + $taskOldPath
    & (Join-Path $JdkHome 'bin\java.exe') -version
    Write-Host "주소: http://127.0.0.1:$Port/"
    Write-Host '종료하려면 Ctrl+C를 누르세요.'
    & .\gradlew.bat bootRun --console=plain "--args=--server.port=$Port"
    if ($LASTEXITCODE -ne 0) { throw "Gradle 종료 코드: $LASTEXITCODE" }
} finally {
    $env:JAVA_HOME = $taskOldJava
    $env:PATH = $taskOldPath
    Pop-Location
}
